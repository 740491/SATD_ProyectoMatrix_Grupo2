/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agentes;

import agentes.AgenteArquitecto.Agente;
import agentes.AgenteArquitecto.tipoAgente;
import jade.core.behaviours.CyclicBehaviour;
import java.util.StringTokenizer;

import agentes.AgenteSistema.Decision;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPAAgentManagement.FailureException;
import java.util.List;
import java.util.Random;

/**
 *
 * @author apied
 */
public class AgenteArquitectoManejador extends CyclicBehaviour {
    private final int MAX_TIMEOUTS = 5;
    private final int TIMEOUT = 2000; //ms
    int contador = 1;
    
    /*
        ATRIBUTOS ARQUITECTO
    */
    List<Agente> agentesResistencia;
    List<Agente> agentesSistema;
    List<Agente> agentesJoePublic;
    
    List<Agente> agentesResistenciaLibres;
    List<Agente> agentesSistemaLibres;
    List<Agente> agentesJoePublicLibres;
    
    AgenteArquitecto.Log log;
   

    AgenteArquitectoManejador(List<Agente> agentesResistencia, List<Agente> agentesSistema, List<Agente> agentesJoePublic, AgenteArquitecto.Log log) {
        this.agentesResistencia = agentesResistencia;
        this.agentesSistema = agentesSistema;
        this.agentesJoePublic = agentesJoePublic;
        
        this.agentesResistenciaLibres = agentesResistencia;
        this.agentesSistemaLibres = agentesSistema;
        this.agentesJoePublicLibres = agentesJoePublic;
        
        this.log = log;
    }
    
    private Agente elegirRandom(tipoAgente tipo){
       Random rand = new Random();
       Agente candidato;
       int randomIndex;
        switch (tipo) {
                case RESISTENCIA:
                    randomIndex = rand.nextInt(agentesResistenciaLibres.size());
                    candidato = agentesResistenciaLibres.get(randomIndex);
                    //se elimina al que ya esta ocupado
                    agentesResistenciaLibres.remove(randomIndex);
                    return candidato;
                   
                case SISTEMA:
                    
                    randomIndex = rand.nextInt(agentesSistemaLibres.size());
                    candidato = agentesSistemaLibres.get(randomIndex);
                    //se elimina al que ya esta ocupado
                    agentesSistemaLibres.remove(randomIndex);
                    return candidato;
                    
                case JOEPUBLIC:
                    randomIndex = rand.nextInt(agentesJoePublicLibres.size());
                    candidato = agentesJoePublicLibres.get(randomIndex);
                    //se elimina al que ya esta ocupado
                    agentesJoePublicLibres.remove(randomIndex);
                    return candidato;
                    
                default:
                    return null;
            }
        
    }
    
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
            
            - RecibirAgente Resistencia: Tipo = INFORM, Content = AgenteResistencia, AID
            
            - RecibirAgente Sistema: Tipo = INFORM, Content = AgenteSistema, AID
            
            - RecibirAgenteJoePublic: Tipo = INFORM, Content = AgenteJoePublic, AID
            
            - Recibir Información: Tipo = INFORM, Content = Informacion, LoQueMePodaisMandar
            - A los agree de las peticiones de arriba no les he pedido content, creo que no hace falta
            - ConocerOraculo: TIpo: REQUEST, Content: ConocerOraculo (arquitecto reenvía esta peticion a smith o neo cuando oraculo le notifica)
            - ConocerOraculo - Agree: TIpo: AGREE, Content: ConocerOraculo
            - ConocerOraculo - Inform: TIpo: INFORM, Content: ConocerOraculo
            - Faltaría un mensaje de inicio o algo asi
        */
        /*
        - Solicitar Agente Resistencia: Tipo = QUERY_REF, Content = AgenteResistencia
        - Solicitar Agente Sistema: Tipo = QUERY_REF, Content = AgenteSistema
        - Solicitar AgenteJoePublic: Tipo = QUERY_REF, Content = AgenteJoePublic
        - SolicitarInfoEstado: Tipo = QUERY_REF, Content = Informacion
         */
        switch (mensaje.getPerformative()) {
            case ACLMessage.QUERY_REF:
                tratarQueryRef(mensaje);
                break;
            case ACLMessage.REQUEST:
                break;
            case ACLMessage.AGREE:
                break;
            case ACLMessage.INFORM:
                break;
            case ACLMessage.REFUSE:
                break;
            default:
                break;
        }
        
        
    }
    
    private void tratarQueryRef(ACLMessage msg){
        
        String content[] = msg.getContent().split(",");
        // CASOS EN LOS QUE SE PUEDE RECIBIR UN QUERY_REF
        
        // ESTADO DEL SISTEMA
        if (content[0].equals("ESTADOSISTEMA")) {
            // Confirmar (agree)
            ACLMessage agree = msg.createReply();
            agree.setPerformative(ACLMessage.AGREE);
            this.myAgent.send(agree);
            
            // Seleccionar estado del sistema -> String a parsear, clase... DECIDIR
            // TODO
            //estado = ...
            
            // Enviar respuesta (inform)
            ACLMessage inform = msg.createReply();
            inform.setPerformative(ACLMessage.INFORM_REF);
            //inform.setContentObject(estado);
            this.myAgent.send(inform);
        }
        // COMBATIR
        else if(content[0].equals("COMBATIR")){
            // Determinar el bando de quién ha enviado el mensaje
            Agente a;
            if(content[1].equals("SISTEMA")){
                // tomar agente random de la resistencia (agentesResistencia)
                a = elegirRandom(tipoAgente.RESISTENCIA);
                
            }
            else{
                // tomar agente random del sistema (agentesSistema)
                 a = elegirRandom(tipoAgente.SISTEMA);
            }
            
            // Enviar request y esperar respuestas para confirmar al que inició la petición
            // no se que enviarle...
            //ACLMessage query = new ACLMessage(ACLMessage.REQUEST);
            //query.addReceiver(new AID(a.getNombre(), AID.ISLOCALNAME));
            //query.setContent("Combatir," + String.valueOf(content[2]));
            //this.myAgent.send(query);
        }
        // RECLUTAR
        else{
            Agente a;
            Random random = new Random();
            // tomar agente JoePublic random (agentesJoePublic)
            
            // Enviar request y esperar respuestas para confirmar al que inició la petición
        }
        
        
    }
    
    // Como resultado del combate o como resultado del reclutamiento
    private void tratarRequest(ACLMessage msg){
    
    }
    
    // Sólo se puede producir si el agente a emparejar está ocupado (O SI SE ASIGNA UNO DIRECTAMENTE EN EL QUERY_REF -> BORRAR)
    private void tratarRefuse(ACLMessage msg){
    
    }
    
    // En caso del combate si el agente a emparejar acepta (O SI SE ASIGNA UNO DIRECTAMENTE EN EL QUERY_REF -> BORRAR)
    private void tratarAgree(ACLMessage msg){
    
    }
    
    // En caso del combate si el agente a emparejar informa (O SI SE ASIGNA UNO DIRECTAMENTE EN EL QUERY_REF -> BORRAR)
    private void tratarInform(ACLMessage msg){
    
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
