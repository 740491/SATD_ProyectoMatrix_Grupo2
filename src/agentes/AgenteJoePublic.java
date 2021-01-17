/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agentes;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.Random;

/**
 *
 * @author nacho
 */
public class AgenteJoePublic extends Agent {
    
    //---------------------------------- CONSTANTES ----------------------------------
    private final int MAX_TIMEOUTS = 3;
    private final int TIMEOUT = 5000; //ms

    
    public class JoePublic_behaviour extends CyclicBehaviour {
        /* Comportamiento Agente JoePublic
        * 
        * Blocking receive hasta recibir REQUEST desde AgenteSistema o AgenteResistencia
        * 
        * RECLUTAR,tipoAgente, bonus (prob reclutamiento), RECLUTAR (exito o fracaso)
        * Gestionar si el reclutamiento es para uno u otro agente
        * Probabilidad te dejas reclutar o no
        * 
        * Mandar agree de ok reclutado
        *
        * REQUEST a AgenteArquitecto
        * */
        @Override
        public void action(){
            ACLMessage mensaje = myAgent.blockingReceive();           
            if(ACLMessage.REQUEST == mensaje.getPerformative() ){
                
                ACLMessage agree = new ACLMessage(ACLMessage.AGREE);
                agree.addReceiver(mensaje.getSender());
                this.myAgent.send(agree);
            
            
                String content[] = mensaje.getContent().split(",");
                
                if(content[0].equals(MensajesComunes.tipoAccion.CONOCERORACULO.name())){
                    /*Oráculo*/
                    ACLMessage bonus = new ACLMessage(ACLMessage.REQUEST);
                    if(content[1].equals(MensajesComunes.tipoAgente.SISTEMA.name())){
                        bonus.addReceiver(new AID("Smith", AID.ISLOCALNAME));
                    }
                    else{
                        bonus.addReceiver(new AID("Neo", AID.ISLOCALNAME));
                    }
                    bonus.setContent(MensajesComunes.tipoAccion.CONOCERORACULO.name());
                    
                    ACLMessage okey = new ACLMessage(ACLMessage.AGREE);
                    okey.addReceiver(mensaje.getSender());
                    this.myAgent.send(okey);
                    //esperar agree de recopilador
                    // while(not agree)
                    boolean recopilador_agree = false;
                    
                    int timeouts = MAX_TIMEOUTS;
                    while(!recopilador_agree && timeouts > 0){
                        ACLMessage respuesta = this.myAgent.blockingReceive(TIMEOUT);

                        // Si salta timeout -> respuesta = null
                        // Reenviar la petición (y no cambiar recopilador_agree)
                        if(respuesta == null){
                            this.myAgent.send(bonus);
                            timeouts--;
                            System.out.println("SALTA TIMEOUT-ORACULO hacia- " + content[1]);
                        }
                        // Si la respuesta es un "agree" es lo que esperábamos
                        // Salir del bucle (recopilador_agree = true)
                        // Si no es un agree se ignora el mensaje
                        else if(ACLMessage.AGREE == respuesta.getPerformative() ){
                            recopilador_agree = true;
                            timeouts = MAX_TIMEOUTS;
                            //System.out.println("RECIBIDO-DM "  + this.myAgent.getLocalName());
                        }
                    }
                    // esperar inform
                    boolean recopilador_inform = false;
                    while(!recopilador_inform && timeouts > 0){
                        ACLMessage inform_respuesta = this.myAgent.blockingReceive(TIMEOUT);

                        // Si salta timeout -> inform_respuesta = null
                        // Reenviar la petición (y no cambiar recopilador_inform)
                        if(inform_respuesta == null){
                            this.myAgent.send(bonus);
                            timeouts--;
                        }

                        // Si la respuesta es un "inform" es lo que esperábamos
                        // Salir del bucle (recopilador_inform = true)
                        // Si no es un inform se ignora el mensaje
                        else if(ACLMessage.INFORM == inform_respuesta.getPerformative() ){
                            recopilador_inform = true;
                            timeouts = MAX_TIMEOUTS;
                        }
                    }
                    
                    ACLMessage inform = new ACLMessage(ACLMessage.INFORM);
                    inform.addReceiver(mensaje.getSender());
                    if(timeouts == 0){
                        System.out.println("ORACULEAMOS MAL");
                        inform.setContent(MensajesComunes.tipoAccion.CONOCERORACULO.name() + "," + MensajesComunes.tipoResultado.FRACASO.name());
                    }else{
                        System.out.println("ORACULEAMOS BIEN");
                        inform.setContent(MensajesComunes.tipoAccion.CONOCERORACULO.name() + "," + MensajesComunes.tipoResultado.ORACULO.name());

                    }
                    System.out.println("Informar del oráculo");
                    this.myAgent.send(inform);
                    this.myAgent.doDelete();
                }
                else{
                    Random rand = new Random();
                    float prob = rand.nextFloat();
                    if(prob*100 < Integer.parseInt(content[2]) ){
                        /*Mando respuesta de reclutar*/
                        ACLMessage respuesta = new ACLMessage(ACLMessage.INFORM);
                        respuesta.addReceiver(mensaje.getSender());
                        respuesta.setContent(MensajesComunes.tipoAccion.RECLUTAMIENTO.name() + "," + MensajesComunes.tipoResultado.EXITO.name());
                        System.out.println("Éxito al reclutar.");
                        this.myAgent.send(respuesta);
                        this.myAgent.doDelete();
                    }else{
                        /*Mando respuesta de no reclutar*/
                        ACLMessage respuesta = new ACLMessage(ACLMessage.INFORM);
                        respuesta.addReceiver(mensaje.getSender());
                        respuesta.setContent(MensajesComunes.tipoAccion.RECLUTAMIENTO.name() + "," + MensajesComunes.tipoResultado.FRACASO.name());
                        this.myAgent.send(respuesta);
                        if(content[1].equals(MensajesComunes.tipoAgente.SISTEMA.name())){
                            /*Se recibe el REQUEST del Agente Sistema*/
                            System.out.println("Fracaso al reclutar: El agente " + this.myAgent.getName() + " ha sido asesinado ");
                            this.myAgent.doDelete();
                        }
                    }
                }
                
            }
        }
    }
    protected void setup()
    {
        MessageTemplate protocolo = MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);// Crear un MessageTemplate de tipo FIPA_REQUEST;
        MessageTemplate performativa = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);// Asignar una Performativa de tipo REQUEST al objeto MessageTemplate
 
        this.addBehaviour(new JoePublic_behaviour());
    }
    
    protected void takeDown(){
        System.out.println("El agente " + getLocalName() + " muere");
    }    
}
