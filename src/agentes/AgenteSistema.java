/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agentes;

/**111
 *
 * @author Tisho
 */
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.Random;


public class AgenteSistema extends Agent {
    //---------------------------------- CONSTANTES ----------------------------------
    private final int MAX_TIMEOUTS = 5;
    private final int TIMEOUT = 2000; //ms
    private Estrategias estrategia = Estrategias.ALEATORIA; //Estrategia a utilizar
    
    //Diferentes estrategias de toma de decisión a probar. Esta con un enum por si quieres ponerles nombre
    public enum Estrategias{
        ALEATORIA,
        ESTRATEGIA1,
        ESTRATEGIA2,
        ESTRATEGIA3,
    }
    
    public enum Decision{
        COMBATIR, RECLUTAR, PEDIR_INFO
    }
    
   
    
    //---------------------------------- VARIABLES GLOBALES ----------------------------------
    private int bonus;
    private int max_bonus;
    private int timeouts;
    private boolean ocupado;
    private Random rand;
    AID arquitecto;
    String objetivo; //AID del agente a atacar o reclutar
    
    //---------------------------------- ESTRATEGIAS ----------------------------------
    
    public Decision estrategia_aleatoria(){
        float prob = rand.nextFloat();
        if(prob > 0.5){
            return Decision.COMBATIR;
        }else{
            return Decision.RECLUTAR;
        }
    }
    
    // TODO
    public Decision estrategia1(){
        float prob = rand.nextFloat();
        if(prob > 0.5){
            return Decision.COMBATIR;
        }else{
            return Decision.RECLUTAR;
        }
    }
    
    // TODO
    public Decision estrategia2(){
        float prob = rand.nextFloat();
        if(prob > 0.5){
            return Decision.COMBATIR;
        }else{
            return Decision.RECLUTAR;
        }
    }
    
    // TODO
    public Decision estrategia3(){
        float prob = rand.nextFloat();
        if(prob > 0.5){
            return Decision.COMBATIR;
        }else{
            return Decision.RECLUTAR;
        }
    }
        
    
    //---------------------------------- FUNCIONES Y METODOS ----------------------------------
    public class Sistema_behaviour extends CyclicBehaviour {
        
        // Solicitar combate con otro agente
        public Decision decidir_accion(){
            Decision resultado;
            switch(estrategia){
                case ESTRATEGIA1:
                    resultado = estrategia1();
                    break;
                case ESTRATEGIA2:
                    resultado = estrategia2();
                    break;
                case ESTRATEGIA3:
                    resultado = estrategia3();
                    break;
                default:
                    resultado = estrategia_aleatoria();
                    break;
            }
            return resultado;
        }
        
        
        public void morir(){
            boolean recopilador_agree = false;
            ACLMessage mensaje_muerte = new ACLMessage(ACLMessage.REQUEST);
            mensaje_muerte.setContent("Muerte");
            mensaje_muerte.addReceiver(arquitecto);
            while(!recopilador_agree){
                ACLMessage respuesta = this.myAgent.blockingReceive(TIMEOUT);
                if(respuesta == null) this.myAgent.send(mensaje_muerte);
                else if(ACLMessage.AGREE == respuesta.getPerformative()) recopilador_agree = true;
            }
            myAgent.doDelete();
        }
        
        //Trata una petición de combate, incluyendo el cálculo de probabiliades, y devuelve un string con el resultado
        public String tratar_combate(float bonus_rival){
            //float resultado = rand.nextFloat() * bonus/bonus_rival;
            float resultado = rand.nextFloat() * 1 + (bonus-bonus_rival)/10;      
            if(resultado > 0.55){ //Victoria
                if(bonus < max_bonus) bonus++;
                return "Victoria";
            }else if(resultado >= 0.45){ //Tablas
                bonus--;
                return "Tablas";
            }else{ //Derrota
                return "Derrota";
            }
        }
        
        @Override
        public void action() {
            //Leer mensaje (con block corto para hacer de espera entre acciones)
            ACLMessage mensaje = myAgent.blockingReceive(TIMEOUT);

            //Si salta timeout
            if(mensaje == null){
                if( !ocupado ){ //Estamos libres, a hacer algo
                    Decision dec = decidir_accion();
                    ocupado = true;
                    if(dec == Decision.COMBATIR){
                        ACLMessage query = new ACLMessage(ACLMessage.QUERY_REF);
                        query.addReceiver(arquitecto);
                        query.setContent("AgenteResistencia");//TODO: AgenteSistema
                        this.myAgent.send(query);

                        timeouts = MAX_TIMEOUTS;
                    }else if(dec == Decision.RECLUTAR){
                        ACLMessage query = new ACLMessage(ACLMessage.QUERY_REF);
                        query.addReceiver(arquitecto);
                        query.setContent("AgenteJoePublic");
                        this.myAgent.send(query);
                    }
                    else if(dec == Decision.PEDIR_INFO){
                        ACLMessage query = new ACLMessage(ACLMessage.QUERY_REF);
                        query.addReceiver(arquitecto);
                        query.setContent("Informacion");
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
            else if(ACLMessage.INFORM == mensaje.getPerformative() ){
                timeouts = MAX_TIMEOUTS;
                String content[] = mensaje.getContent().split(",");
                if(content[0] == "AgenteResistencia"){
                        ACLMessage query = new ACLMessage(ACLMessage.REQUEST);
                        query.addReceiver(new AID(content[1], AID.ISLOCALNAME));
                        query.setContent("Combatir," + String.valueOf(bonus));
                        this.myAgent.send(query);
                }else if(content[0] == "AgenteJoePublic"){
                        ACLMessage query = new ACLMessage(ACLMessage.REQUEST);
                        query.addReceiver(new AID(content[1], AID.ISLOCALNAME));
                        query.setContent("Reclutar," + String.valueOf(bonus));
                        this.myAgent.send(query);
                }else if(content[0] == "AgenteOraculo"){
                        ACLMessage query = new ACLMessage(ACLMessage.REQUEST);
                        query.addReceiver(new AID(content[1], AID.ISLOCALNAME));
                        query.setContent("ConocerOraculo");
                        this.myAgent.send(query);
                }else if(content[0] == "Informacion"){
                    ocupado = false; 
                    //TODO: Actualizar info
                }else if(content[0] == "ResultadoCombate"){
                    ocupado = false; 
                    if(content[1] == "Victoria"){
                        if(bonus < max_bonus) bonus++;
                    }else if(content[1]=="Tablas"){
                        bonus--;
                    }else{
                       morir();
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
                if(content[0] == "Combatir"){
                    if(!ocupado){ //Si estamos libres -> tratar combate
                        //Confirmar
                        ACLMessage agree = new ACLMessage(ACLMessage.AGREE);
                        agree.addReceiver(mensaje.getSender());
                        this.myAgent.send(agree);

                        String res = tratar_combate(Integer.parseInt(content[1]));

                        //Informar
                        ACLMessage inform = new ACLMessage(ACLMessage.INFORM);
                        inform.addReceiver(mensaje.getSender());
                        inform.setContent(res);
                        this.myAgent.send(inform);
                        if(res=="Derrota"){ //Morir
                            //Informar a arquitecto
                            morir();
                        }
                    }else if(content[0] == "ConocerOraculo" && (this.myAgent.getLocalName().contains("Neo") || this.myAgent.getLocalName().contains("Smith"))){
                        ACLMessage agree = new ACLMessage(ACLMessage.AGREE);
                        agree.setContent("ConocerOraculo");
                        agree.addReceiver(mensaje.getSender());
                        this.myAgent.send(agree);
                        
                        bonus+=5;
                        if(bonus>max_bonus) bonus=max_bonus;
                        
                        ACLMessage inform = new ACLMessage(ACLMessage.INFORM);
                        inform.addReceiver(mensaje.getSender());
                        inform.setContent("ConocerOraculo");
                        this.myAgent.send(inform);
                        
                    }else{//Si no -> Rechazar
                        ACLMessage respuesta = new ACLMessage(ACLMessage.REFUSE);
                        respuesta.addReceiver(mensaje.getSender());
                    }
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
        
        //TODO: Envíar mensaje a arquitecto
        ACLMessage inform = new ACLMessage(ACLMessage.INFORM);
        inform.addReceiver(arquitecto);
        inform.setContent("Creado");
        this.send(inform);
    }
}
