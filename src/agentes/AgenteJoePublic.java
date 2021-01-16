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
                Random rand = new Random();
                float prob = rand.nextFloat();
                System.out.println("Probabilidad de reclutar creada " + prob);
                if(prob < 0.8){
                    /*Mando respuesta de reclutar*/
                    ACLMessage respuesta = new ACLMessage(ACLMessage.INFORM);
                    respuesta.addReceiver(mensaje.getSender());
                    respuesta.setContent(MensajesComunes.tipoResultado.EXITO.name());
                    System.out.println("Éxito al reclutar.");
                    this.myAgent.send(respuesta);
                    this.myAgent.doDelete();
                }else{
                    /*Mando respuesta de no reclutar*/
                    ACLMessage respuesta = new ACLMessage(ACLMessage.INFORM);
                    respuesta.addReceiver(mensaje.getSender());
                    respuesta.setContent(MensajesComunes.tipoResultado.FRACASO.name());
                    System.out.println("Fracaso al reclutar.");
                    this.myAgent.send(respuesta);
                    if(content[1].equals(MensajesComunes.tipoAgente.SISTEMA.name())){
                        /*Se recibe el REQUEST del Agente Sistema*/
                        System.out.println("El agente " + this.myAgent.getName() + " es de tipo " + content[0]);
                        this.myAgent.doDelete();
                        System.out.println("Eliminado el agente.");
                    }
                }
            }
        }
    }
    protected void setup()
    {
        MessageTemplate protocolo = MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);// Crear un MessageTemplate de tipo FIPA_REQUEST;
        MessageTemplate performativa = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);// Asignar una Performativa de tipo REQUEST al objeto MessageTemplate
        MessageTemplate plantilla = MessageTemplate.and(protocolo,performativa); //Componer Plantilla con las anteriores
 
        addBehaviour(new AgenteJoePublicManejador(this, plantilla));
    }
    
    protected void takeDown(){
        System.out.println("El agente " + getLocalName() + " muere");
    }    
    
}
