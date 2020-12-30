/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agentes;

import jade.core.behaviours.Behaviour;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

/**
 *
 * @author nacho
 */
public class AgenteSistemaManejador extends AchieveREResponder {

    public AgenteSistemaManejador(AgenteSistema a, MessageTemplate plantilla) {
        super(a, plantilla);
    }
    
    protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
    }
    protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException {
    }
    
    protected void handleAgree(ACLMessage agree) {
    
    }
    protected void handleRefuse(ACLMessage refuse) {

    }

    protected void handleInform(ACLMessage inform) {

    }

    protected void handleFailure(ACLMessage failure) {

    }      
    
}
