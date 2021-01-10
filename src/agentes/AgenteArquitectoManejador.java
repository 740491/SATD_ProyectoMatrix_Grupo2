/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agentes;

import java.util.StringTokenizer;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPAAgentManagement.FailureException;

/**
 *
 * @author apied
 */
public class AgenteArquitectoManejador extends AchieveREResponder {
    
    public AgenteArquitectoManejador(AgenteArquitecto arquitecto, MessageTemplate mt) {
        super(arquitecto, mt);
    }
    
    
    protected ACLMessage handleQueryRef(ACLMessage queryRef) throws NotUnderstoodException, RefuseException {
        
    }
    protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException {
    }
    
    protected void handleAgree(ACLMessage agree) {
    
    }
    protected void handleRefuse(ACLMessage refuse) {
        
        // EN COMBATE
        // Si el combatiente que ha buscado el arquitecto está ocupado (segundo luchador):
        
        // REFUSE AL QUE HA INICIADO EL COMBATE
        // Para que vuelva a decidir acción
        
        
        // EN RECLUTAMIENTO
        // Si el JoePublic que ha buscado el arquitecto está ocupado (ya lo están intentando reclutar):
        
        // REFUSE AL QUE HA INICIADO EL RECLUTAMIENTO
        // Para que vuelva a decidir acción

    }

    protected void handleInform(ACLMessage inform) {

    }

    protected void handleFailure(ACLMessage failure) {
        if (failure.getSender().equals(myAgent.getAMS())) {
                // FAILURE notification from the JADE runtime: the receiver
                // does not exist
                System.out.println("ARQUITECTO: Responder does not exist");
        }
        else {
                System.out.println("ARQUITECTO: Agent "+failure.getSender().getName()+" failed to perform the requested action");
        }
    }   
    
}
