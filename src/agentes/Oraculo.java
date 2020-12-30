/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agentes;

import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 *
 * @author nacho
 */
public class Oraculo extends Agent {
    protected void setup()
    {
        MessageTemplate protocolo = MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);// Crear un MessageTemplate de tipo FIPA_REQUEST;
        MessageTemplate performativa = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);// Asignar una Performativa de tipo REQUEST al objeto MessageTemplate
        MessageTemplate plantilla = MessageTemplate.and(protocolo,performativa); //Componer Plantilla con las anteriores
 
        addBehaviour(new AgenteOraculoManejador(this, plantilla));
    }
    
    protected void takeDown(){
        System.out.println("El agente " + getLocalName() + " muere");
    }    
}
