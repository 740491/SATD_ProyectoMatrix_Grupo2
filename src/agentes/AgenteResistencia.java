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
import agentes.MensajesComunes.tipoAccion;
import agentes.Decisor;
import agentes.Decisor.tipoDecision;
import agentes.Decisor.Estrategias;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AgenteResistencia extends Agent {
    //---------------------------------- CONSTANTES ----------------------------------
    private final int MAX_TIMEOUTS = 3;
    private final int TIMEOUT = 2000; //ms
    
    //---------------------------------- VARIABLES GLOBALES ----------------------------------
    private Decisor decisor = new Decisor(Estrategias.DESCENDENTE); // IMPORTANTE: Estrategia a utilizar
    private int bonus;
    private int max_bonus;
    private int timeouts;
    private boolean ocupado;
    private Random rand;
    AID arquitecto;
    String objetivo; //AID del agente a atacar o reclutar
    boolean tengoInfo;
        
    
    //---------------------------------- FUNCIONES Y METODOS ----------------------------------
    public class Sistema_behaviour extends CyclicBehaviour {

        //Avisa al arquitecto con bucles para reintentar y todo
        public void avisar_arquitecto(String contenido){
            boolean recopilador_agree = false;
            boolean recopilador_inform = false;
            
            ACLMessage resultado = new ACLMessage(ACLMessage.REQUEST);
            resultado.addReceiver(arquitecto);
            resultado.setContent(contenido);
            this.myAgent.send(resultado);
            while(!recopilador_agree){
                ACLMessage respuesta = this.myAgent.blockingReceive(TIMEOUT);
                if(respuesta == null) this.myAgent.send(resultado);
                else if(ACLMessage.AGREE == respuesta.getPerformative()){
                    recopilador_agree = true;
                }
            }
            while(!recopilador_inform){
                ACLMessage respuesta = this.myAgent.blockingReceive(TIMEOUT);
                if(respuesta == null) System.out.println("AGENTE SISTEMA/RESISTENCIA SE QUEDA PILLADO Y NO SE QUE HACER");
                else if(ACLMessage.INFORM == respuesta.getPerformative()){
                    recopilador_inform = true;
                    //System.out.println("BIEN!! El arquitecto confirma");
                }
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
        
        public tipoResultado rotar(String res1){
            if(res1.equals(tipoResultado.EXITO.name())){
                return tipoResultado.FRACASO;
            }else if(res1.equals(tipoResultado.FRACASO.name())){
                return tipoResultado.EXITO;
            }else{
                return tipoResultado.EMPATE;
            }
        }
        
        @Override
        public void action() {
            
            // HACIENDO COSAS EN MATRIX...
            try {
                Thread.sleep(rand.nextInt(1500));
            } catch (InterruptedException ex) {
                Logger.getLogger(AgenteResistencia.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            //Leer mensaje (con block corto para hacer de espera entre acciones)
            //System.out.println("Leemos mensaje: " + this.myAgent.getLocalName());
            ACLMessage mensaje = myAgent.blockingReceive(TIMEOUT);

            //Si salta timeout
            if(mensaje == null){
                if( !ocupado ){ //Estamos libres, a hacer algo
                    if(tengoInfo){
                        tipoDecision dec = decisor.decidir_accion();
                        ocupado = true;
                        if(dec == tipoDecision.COMBATE){
                            ACLMessage query = new ACLMessage(ACLMessage.QUERY_REF);
                            query.addReceiver(arquitecto);
                            query.setContent(tipoMensaje.ATACAR.name() + "," + tipoAgente.RESISTENCIA.name() + "," + Integer.toString(bonus));
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
                        tengoInfo=false;
                    }
                    else{
                        // PEDIR INFORMACIÓN
                        ACLMessage query = new ACLMessage(ACLMessage.QUERY_REF);
                        query.addReceiver(arquitecto);
                        query.setContent(tipoMensaje.PEDIRINFORMACION.name());
                        this.myAgent.send(query);
                        
                        
                        boolean recopilador_agree = false;
                        boolean recopilador_inform = false;
                        // ESPERAR AGREE
                        while(!recopilador_agree){
                            ACLMessage respuesta = this.myAgent.blockingReceive(TIMEOUT);
                            if(respuesta == null) this.myAgent.send(query);
                            else if(ACLMessage.AGREE == respuesta.getPerformative()){
                                recopilador_agree = true;
                            }
                        }
                        
                        String info = "";
                        // ESPERAR INFORM
                        while(!recopilador_inform){
                            ACLMessage respuesta = this.myAgent.blockingReceive(TIMEOUT);
                            if(respuesta == null) System.out.println("AGENTE SISTEMA: " + this.myAgent.getLocalName() + "esperando inform del arquitecto");
                            else if(ACLMessage.INFORM_REF == respuesta.getPerformative()){
                                recopilador_inform = true;
                                info = respuesta.getContent();
                            }
                        }
                        
                        // Modificar estrategia en función del inform
                        decisor.actualizar_info(info);
                        tengoInfo=true;
                    }
                }
                else{
                    timeouts--;
                    if(timeouts <= 0){
                        System.out.println("SALTA TIMEOUT!!!!");
                        ocupado = false; // Se acabo el tiempo de espera
                        avisar_arquitecto(tipoMensaje.ESTOYLIBRE.name() + "," + tipoAgente.RESISTENCIA.name());
                    }
                }
            }
            else if(ACLMessage.AGREE == mensaje.getPerformative() ){
                ocupado = true;
                timeouts = MAX_TIMEOUTS;
            }
            //Nos llega un agente o información
            else if(ACLMessage.INFORM_REF == mensaje.getPerformative() ){
                System.out.println("Soy: " + this.myAgent.getName() + "y el agente " + mensaje.getSender().getLocalName() + " me envia AL AGENTE: "  + mensaje.getContent());
                timeouts = MAX_TIMEOUTS;
                String content[] = mensaje.getContent().split(",");
                
                if(content[0].equals(tipoAgente.SISTEMA.name())){
                        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
                        request.addReceiver(new AID(content[1], AID.ISLOCALNAME));
                        request.setContent(tipoAccion.COMBATE + "," + tipoAgente.RESISTENCIA + "," + String.valueOf(bonus));
                        this.myAgent.send(request);
                }else if(content[0].equals(tipoAgente.JOEPUBLIC.name())){
                        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
                        request.addReceiver(new AID(content[1], AID.ISLOCALNAME));
                        request.setContent(tipoAccion.RECLUTAMIENTO.name() + "," + tipoAgente.RESISTENCIA +  "," + String.valueOf(bonus));
                        this.myAgent.send(request);
                }else if(content[0].equals(tipoAgente.ORACULO.name())){ // ------------------------- 
                        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
                        request.addReceiver(new AID(content[1], AID.ISLOCALNAME));
                        request.setContent(tipoAccion.CONOCERORACULO.name() + "," + tipoAgente.RESISTENCIA);
                        this.myAgent.send(request);
                }
                else if(content[0].equals(tipoMensaje.PEDIRINFORMACION.name())){ //TODO: Actualizar cuando se haga el decisor
                    ocupado = false; 
                    String info = content[1];
                    decisor.actualizar_info(info);
                }else{
                    System.err.println("ERROR: El agente " + this.myAgent.getName() + " recibe INFORM_REF inesperado: " + mensaje.getContent());
                }
            }//mensaje de finalizar una acción
            else if(ACLMessage.INFORM == mensaje.getPerformative() ){
                ocupado = false; 
                
                String content[] = mensaje.getContent().split(",");
                //Envíar resultado a arquitecto TODO: COMPROBAR RECEPCION CON BUCLE
                
                avisar_arquitecto(tipoMensaje.RESULTADO.name() + "," + tipoAgente.RESISTENCIA.name() + "," +
                        content[0] + "," + content[1] + "," + mensaje.getSender().getLocalName());
                
                if(content[0].equals(tipoAccion.COMBATE.name())){
                    System.out.println("Soy: " + this.myAgent.getLocalName() + " y me llega el INFORM DEl COMBATE: "  + mensaje.getContent());
                    ocupado = false;
                    if(content[1].equals(tipoResultado.EXITO.name())){
                        if(bonus < max_bonus) bonus++;
                    }else if(content[1].equals(tipoResultado.EMPATE.name())){
                        bonus--;
                    }else{
                        System.out.println("Descanse en paz, agenge resistencia " + this.myAgent.getLocalName());
                        this.myAgent.doDelete();
                    }
                }else if(content[0].equals(tipoAccion.RECLUTAMIENTO.name())){
                    System.out.println("Soy: " + this.myAgent.getLocalName() + " y me llega el INFORM DEl RECLUTAMIENTO: "  + mensaje.getContent());
                }else if(content[0].equals(tipoAccion.CONOCERORACULO.name())){
                    System.out.println("Soy: " + this.myAgent.getLocalName() + " y me llega el INFORM DEl CONOCERORACULO: "  + mensaje.getContent());
                    if(this.myAgent.getName().equals("Neo") ){ // Oraculo no necesita reenviar, ya nos ha conocido
                        bonus+=5;
                        if(bonus>max_bonus) bonus=max_bonus;
                     }
                }else {
                    System.err.println("ERROR: El agente " + this.myAgent.getName() + " recibe INFORM inesperado: " + content[0]);
                }
            }
            else if(ACLMessage.REFUSE == mensaje.getPerformative() ){
                ocupado = false;
                if(mensaje.getContent().equals("DESOCUPATE")){
                    avisar_arquitecto(tipoMensaje.ESTOYLIBRE.name() + "," + tipoAgente.RESISTENCIA.name());
                }
            }
            else if(ACLMessage.REQUEST == mensaje.getPerformative() ){
                String content[] = mensaje.getContent().split(",");
                System.out.println("Soy: " + this.myAgent.getName() + " estoy: "+ ocupado + " y el agente " + mensaje.getSender().getLocalName() + " me envia REQUEST: "  + mensaje.getContent());
                if(!ocupado){//Si estamos libres -> tratar
                    if(content[0].equals(tipoAccion.COMBATE.name())){ //Combate
                        //Confirmar
                        ACLMessage agree = new ACLMessage(ACLMessage.AGREE);
                        agree.addReceiver(mensaje.getSender());
                        this.myAgent.send(agree);
                        
                        String res = tratar_combate(Integer.parseInt(content[2])).name();
                        String res_enviar = rotar(res).name();
                        //Informar a agente
                        System.out.println("DEVOLVEMOS: " + tipoAccion.COMBATE.name() + "," + res + " del " + this.myAgent.getLocalName());
                        
                        ACLMessage inform = new ACLMessage(ACLMessage.INFORM);
                        inform.addReceiver(mensaje.getSender());
                        inform.setContent(tipoAccion.COMBATE.name() + "," + res_enviar);
                        this.myAgent.send(inform);
                        
                        //Informar a arquitecto
                        if(res.equals(tipoResultado.FRACASO.name())){ //Morir
                            System.out.println("Descanse en paz, agenge resistencia " + this.myAgent.getLocalName());
                            myAgent.doDelete();
                        }
                    }else if(content[0].equals(tipoAccion.CONOCERORACULO.name())){
                        ACLMessage agree = new ACLMessage(ACLMessage.AGREE);
                        agree.setContent(tipoAccion.CONOCERORACULO.name());
                        agree.addReceiver(mensaje.getSender());
                        this.myAgent.send(agree);
                        
                        bonus+=5;
                        if(bonus>max_bonus) bonus=max_bonus;
                        
                        ACLMessage inform = new ACLMessage(ACLMessage.INFORM);
                        inform.setContent(tipoAccion.CONOCERORACULO.name());
                        inform.addReceiver(mensaje.getSender());
                        
                        this.myAgent.send(inform);
                        
                    }else {
                        System.err.println("ERROR: El agente " + this.myAgent.getName() + " recibe REQUEST inesperado: " + content[0]);
                    }
                }else{//Si no -> Rechazar
                        System.out.println("EL AGENTE: " + this.myAgent.getLocalName() + " ESTA OCUPADO!");
                        ACLMessage respuesta = new ACLMessage(ACLMessage.REFUSE);
                        respuesta.setContent("DESOCUPATE");
                        respuesta.addReceiver(mensaje.getSender());
                        //faltaba esto
                        this.myAgent.send(respuesta);

                }
            }
        }
    }
    
    /*Asignacion de comportamientos*/
    protected void setup() {
        System.out.println("Agente Resistencia "+ getLocalName()+" creado");
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
        tengoInfo = false;
    }
}
