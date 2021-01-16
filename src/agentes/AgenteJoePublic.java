/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agentes;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

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
                String content[] = mensaje.getContent().split(",");
                if(content[0] == MensajesComunes.tipoAgente.SISTEMA.name()){
                    /*Se recibe el REQUEST del Agente Sistema*/
                    ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
                    
                    this.myAgent.send(request);
                }else if(content[0] == MensajesComunes.tipoAgente.RESISTENCIA.name()){
                    /*Se recibe el REQUEST del Agente Resistencia*/
                    ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
                
                    this.myAgent.send(request);
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
