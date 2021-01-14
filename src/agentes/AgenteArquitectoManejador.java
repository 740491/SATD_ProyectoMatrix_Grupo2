/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agentes;

import jade.core.behaviours.CyclicBehaviour;
import java.util.StringTokenizer;

import agentes.AgenteSistema.Decision;
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
public class AgenteArquitectoManejador extends CyclicBehaviour {
    private final int MAX_TIMEOUTS = 5;
    private final int TIMEOUT = 2000; //ms
    int contador = 1;
    
   // public AgenteArquitectoManejador(AgenteArquitecto arquitecto, MessageTemplate mt) {
    //    super(arquitecto, mt);
    //}
    
    
    /*protected ACLMessage handleQueryRef(ACLMessage queryRef) throws NotUnderstoodException, RefuseException {
        
    }
    protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException {
    }*/
    
    @Override
    public void action() {
        //Leer mensaje (con block corto para hacer de espera entre acciones)
        ACLMessage mensaje = myAgent.blockingReceive();
        /*
        Mensajes:
            CON ARQUITECTO:
            - Avisar Creación: Tipo = INFORM, Content = Creado
            - Solicitar Agente Resistencia: Tipo = QUERY_REF, Content = AgenteResistencia
            - RecibirAgente Resistencia: Tipo = INFORM, Content = AgenteResistencia, AID
            - Solicitar Agente Sistema: Tipo = QUERY_REF, Content = AgenteSistema
            - RecibirAgente Sistema: Tipo = INFORM, Content = AgenteSistema, AID
            - Solicitar AgenteJoePublic: Tipo = QUERY_REF, Content = AgenteJoePublic
            - RecibirAgenteJoePublic: Tipo = INFORM, Content = AgenteJoePublic, AID
            - SolicitarInfoParaTOmaDecision: Tipo = QUERY_REF, Content = Informacion
            - Recibir Información: Tipo = INFORM, Content = Informacion, LoQueMePodaisMandar
            - A los agree de las peticiones de arriba no les he pedido content, creo que no hace falta
            - ConocerOraculo: TIpo: REQUEST, Content: ConocerOraculo (arquitecto reenvía esta peticion a smith o neo cuando oraculo le notifica)
            - ConocerOraculo - Agree: TIpo: AGREE, Content: ConocerOraculo
            - ConocerOraculo - Inform: TIpo: INFORM, Content: ConocerOraculo
            - Faltaría un mensaje de inicio o algo asi
        */
        if(mensaje.getPerformative() == ACLMessage.QUERY_REF){
            //Solicitar agente resistencia
        	String content[] = mensaje.getContent().split(",");
            if(content[0] == "AgenteResistencia"){
            	
            }
            //Solicitar agente sistema
            else if(content[0] == "AgenteSistema"){
            	
            }
            //Solicitar agente JoePublic
            else if(content[0] == "AgenteJoePublic"){
            	
            }
            //Solicitar info para toma decision
            else if(content[0] == "Informacion"){
            	Decision dec = decidir_accion();
            }
            //switch(mensaje.getContent()):
        }
        else if(mensaje.getPerformative() == ACLMessage.INFORM){
        	
        }
        else if(mensaje.getPerformative() == ACLMessage.REQUEST){
        	
        }else {
        	
        }
        
        
    }
    
    //protected void handleAgree(ACLMessage agree) {
    
    //}
    //protected void handleRefuse(ACLMessage refuse) {
        
        // EN COMBATE
        // Si el combatiente que ha buscado el arquitecto está ocupado (segundo luchador):
        
        // REFUSE AL QUE HA INICIADO EL COMBATE
        // Para que vuelva a decidir acción
        
        
        // EN RECLUTAMIENTO
        // Si el JoePublic que ha buscado el arquitecto está ocupado (ya lo están intentando reclutar):
        
        // REFUSE AL QUE HA INICIADO EL RECLUTAMIENTO
        // Para que vuelva a decidir acción

    //}

    //protected void handleInform(ACLMessage inform) {

    //}

    //protected void handleFailure(ACLMessage failure) {
      //  if (failure.getSender().equals(myAgent.getAMS())) {
                // FAILURE notification from the JADE runtime: the receiver
                // does not exist
        //        System.out.println("ARQUITECTO: Responder does not exist");
        //}
        //else {
          //      System.out.println("ARQUITECTO: Agent "+failure.getSender().getName()+" failed to perform the requested action");
        //}
    //}   
    
}
