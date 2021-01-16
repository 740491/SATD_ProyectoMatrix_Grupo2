/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agentes;

/**
 *
 * @author Tisho
 */
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.Random;

import agentes.MensajesComunes.tipoMensaje;
import agentes.MensajesComunes.tipoResultado;
import agentes.MensajesComunes.tipoAgente;
import agentes.Decisor;
import agentes.Decisor.tipoDecision;
import agentes.Decisor.Estrategias;


public class AgenteResistencia extends Agent {
    //---------------------------------- CONSTANTES ----------------------------------
    private final int MAX_TIMEOUTS = 5;
    private final int TIMEOUT = 2000; //ms
    
    //---------------------------------- VARIABLES GLOBALES ----------------------------------
    private Decisor decisor = new Decisor(Estrategias.ALEATORIA); // IMPORTANTE: Estrategia a utilizar
    private int bonus;
    private int max_bonus;
    private int timeouts;
    private boolean ocupado;
    private Random rand;
    AID arquitecto;
    String objetivo; //AID del agente a atacar o reclutar
        
    
    //---------------------------------- FUNCIONES Y METODOS ----------------------------------
    public class Sistema_behaviour extends CyclicBehaviour {

        public void avisar_arquitecto(String contenido){ //TODO:
            boolean recopilador_agree = false;
            ACLMessage mensaje_muerte = new ACLMessage(ACLMessage.REQUEST);
            mensaje_muerte.setContent(contenido);
            mensaje_muerte.addReceiver(arquitecto);
            while(!recopilador_agree){
                ACLMessage respuesta = this.myAgent.blockingReceive(TIMEOUT);
                if(respuesta == null) this.myAgent.send(mensaje_muerte);
                else if(ACLMessage.AGREE == respuesta.getPerformative()) recopilador_agree = true;
            }
        }
        
        //Trata una petición de combate, incluyendo el cálculo de probabiliades, y devuelve un string con el resultado
        public tipoResultado tratar_combate(float bonus_rival){
            //float resultado = rand.nextFloat() * bonus/bonus_rival;
            float resultado = rand.nextFloat() * 1 + (bonus-bonus_rival)/10;      
            if(resultado > 0.55){ //Victoria
                if(bonus < max_bonus) bonus++;
                return tipoResultado.EXITO;
            }else if(resultado >= 0.45){ //Tablas
                bonus--;
                return tipoResultado.EMPATE;
            }else{ //Derrota
                return tipoResultado.FRACASO;
            }
        }
        
        @Override
        public void action() {
            //Leer mensaje (con block corto para hacer de espera entre acciones)
            ACLMessage mensaje = myAgent.blockingReceive(TIMEOUT);

            //Si salta timeout
            if(mensaje == null){
                if( !ocupado ){ //Estamos libres, a hacer algo
                    tipoDecision dec = decisor.decidir_accion();
                    ocupado = true;
                    if(dec == tipoDecision.COMBATE){
                        ACLMessage query = new ACLMessage(ACLMessage.QUERY_REF);
                        query.addReceiver(arquitecto);
                        query.setContent(tipoMensaje.ATACAR.name() + "," + tipoAgente.RESISTENCIA.name() + "," + Integer.toString(bonus));//TODO: AgenteSistema
                        this.myAgent.send(query);

                        timeouts = MAX_TIMEOUTS;
                    }else if(dec == tipoDecision.RECLUTAMIENTO){
                        ACLMessage query = new ACLMessage(ACLMessage.QUERY_REF);
                        query.addReceiver(arquitecto);
                        query.setContent(tipoMensaje.RECLUTAR.name() + "," + tipoAgente.RESISTENCIA.name() + "," + Integer.toString(bonus));
                        this.myAgent.send(query);
                    }
                    else if(dec == tipoDecision.PEDIRINFORMACION){
                        ACLMessage query = new ACLMessage(ACLMessage.QUERY_REF);
                        query.addReceiver(arquitecto);
                        query.setContent(tipoMensaje.PEDIRINFORMACION.name());
                        this.myAgent.send(query);
                    }
                }
                else{
                    timeouts--;
                    if(timeouts == 0) ocupado = false; // Se acabo el tiempo de espera
                }
            }
            else if(ACLMessage.AGREE == mensaje.getPerformative() ){
                timeouts = MAX_TIMEOUTS * 2;
            }
            //Nos llega un agente, información, o mensaje de finalizar una acción
            else if(ACLMessage.INFORM_REF == mensaje.getPerformative() ){
                timeouts = MAX_TIMEOUTS;
                String content[] = mensaje.getContent().split(",");
                if(content[0] == "RESISTENCIA"){
                        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
                        request.addReceiver(new AID(content[1], AID.ISLOCALNAME));
                        request.setContent("Combatir," + String.valueOf(bonus));
                        this.myAgent.send(request);
                }else if(content[0] == "JOEPUBLIC"){
                        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
                        request.addReceiver(new AID(content[1], AID.ISLOCALNAME));
                        request.setContent("Reclutar-Resistencia" + String.valueOf(bonus));
                        this.myAgent.send(request);
                /*else if(content[0] == "ORACULO"){ // ------------------------- 
                        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
                        request.addReceiver(new AID(content[1], AID.ISLOCALNAME));
                        request.setContent("ConocerOraculo");
                        this.myAgent.send(request);*/
                }else if(content[0] == "Informacion"){
                    ocupado = false; 
                    String info = null;
                    decisor.actualizar_info(info);
                    //TODO: Actualizar info
                }else if(content[0] == "ResultadoCombate"){
                    ocupado = false;
                    ACLMessage query = new ACLMessage(ACLMessage.REQUEST);
                    query.addReceiver(arquitecto);
                    if(content[1] == "EXITO"){
                        query.setContent("EXITO");
                        if(bonus < max_bonus) bonus++;
                    }else if(content[1]=="EMPATE"){
                        query.setContent("EMPATE");
                        bonus--;
                    }else{
                        query.setContent("EMPATE");
                        //morir();
                    }
                }else if(content[0] == "Reclutar"){
                    ocupado = false; 
                }else if(content[0] == "ConocerOraculo"){
                    ocupado = false; 
                }else {
                    System.out.println("ERROR: El agente " + this.myAgent.getName() + " recibe INFORM inesperado: " + content[0]);
                }
            }
            else if(ACLMessage.REFUSE == mensaje.getPerformative() ){
                ocupado = false;
            }
            else if(ACLMessage.REQUEST == mensaje.getPerformative() ){ //Mensaje de petición de combate
                String content[] = mensaje.getContent().split(",");
                if(!ocupado){
                    if(!ocupado){ //Si estamos libres -> tratar
                        //Confirmar
                        ACLMessage agree = new ACLMessage(ACLMessage.AGREE);
                        agree.addReceiver(mensaje.getSender());
                        this.myAgent.send(agree);

                        String res = tratar_combate(Integer.parseInt(content[1])).name();

                        //Informar a agente
                        ACLMessage inform = new ACLMessage(ACLMessage.INFORM);
                        inform.addReceiver(mensaje.getSender());
                        inform.setContent(res);
                        this.myAgent.send(inform);
                        
                        //Informar a arquitecto
                        ACLMessage inform_ref = new ACLMessage(ACLMessage.INFORM);
                        inform.addReceiver(mensaje.getSender());
                        inform.setContent(res);
                        this.myAgent.send(inform);
                        if(res==tipoResultado.FRACASO.name()){ //Morir
                            myAgent.doDelete();
                        }
                    }else if(content[0] == tipoMensaje.CONOCERORACULO.name() && (this.myAgent.getLocalName().contains("Neo") || this.myAgent.getLocalName().contains("Smith"))){
                        ACLMessage agree = new ACLMessage(ACLMessage.AGREE);
                        agree.setContent(tipoMensaje.CONOCERORACULO.name());
                        agree.addReceiver(mensaje.getSender());
                        this.myAgent.send(agree);
                        
                        bonus+=5;
                        if(bonus>max_bonus) bonus=max_bonus;
                        
                        ACLMessage inform = new ACLMessage(ACLMessage.INFORM);
                        inform.addReceiver(mensaje.getSender());
                        inform.setContent("ConocerOraculo");
                        this.myAgent.send(inform);
                        
                    }
                }else{//Si no -> Rechazar
                        ACLMessage respuesta = new ACLMessage(ACLMessage.REFUSE);
                        respuesta.addReceiver(mensaje.getSender());
                }
            }
        }
    }
    
    /*Asignacion de comportamientos*/
    protected void setup() {
        Object[] args = getArguments();
        bonus = 50;
        ocupado = false;
        rand = new Random();
        this.addBehaviour(new Sistema_behaviour());
        arquitecto = new AID(args[0].toString(), AID.ISLOCALNAME );
        if(this.getLocalName().contains("Neo") || this.getLocalName().contains("Smith")){
            max_bonus = 99;
        }else{
            max_bonus = 90;
        }
    }
}
