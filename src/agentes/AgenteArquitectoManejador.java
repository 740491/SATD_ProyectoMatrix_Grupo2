/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agentes;

import agentes.Agente;
import agentes.MensajesComunes.tipoAccion;
import agentes.MensajesComunes.tipoAgente;
import agentes.MensajesComunes.tipoMensaje;
import agentes.MensajesComunes.tipoResultado;

import jade.core.behaviours.CyclicBehaviour;
import java.util.StringTokenizer;

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
    
    List<Evento> log;
    
    AID resultado;
   

    AgenteArquitectoManejador(List<Agente> agentesResistencia, List<Agente> agentesSistema, List<Agente> agentesJoePublic, List<Evento> log) {
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
                int indice_oraculo = encontrarOraculo(agentesJoePublic);
                randomIndex = rand.nextInt(agentesJoePublicLibres.size());
                candidato = agentesJoePublicLibres.get(randomIndex);
                //se elimina al que ya esta ocupado
                agentesJoePublicLibres.remove(randomIndex);
                return candidato;

            default:
                return null;
        }
        
    }
    private int encontrarOraculo(List<Agente> agentesJoePublic) {
        Agente a = new Agente("oraculo",tipoAgente.JOEPUBLIC);
        return agentesJoePublic.indexOf(a);
        
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
                tratarRequest(mensaje);
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
        
        //comprobar si ha terminado la simulación
        comprobarFinal();
        
        
    }
    
    private void comprobarFinal() {
        if(agentesResistencia.isEmpty() || agentesSistema.isEmpty()){
            boolean recopilador_agree = false;
            ACLMessage mensaje_resultado = new ACLMessage(ACLMessage.REQUEST);
            mensaje_resultado.setContent(log.toString());
            mensaje_resultado.addReceiver(resultado);
            while(!recopilador_agree){
                ACLMessage respuesta = this.myAgent.blockingReceive(TIMEOUT);
                if(respuesta == null) this.myAgent.send(mensaje_resultado);
                else if(ACLMessage.AGREE == respuesta.getPerformative()) recopilador_agree = true;
            }
            
            // esperar inform
            boolean recopilador_inform = false;
            while(!recopilador_inform){
                ACLMessage inform_respuesta = this.myAgent.blockingReceive(TIMEOUT);

                // Si salta timeout -> inform_respuesta = null
                // Reenviar la petición (y no cambiar recopilador_inform)
                if(inform_respuesta == null){
                    this.myAgent.send(mensaje_resultado);
                }

                // Si la respuesta es un "inform" es lo que esperábamos
                // Salir del bucle (recopilador_inform = true)
                // Si no es un inform se ignora el mensaje
                else if(ACLMessage.INFORM == inform_respuesta.getPerformative() ){
                    recopilador_inform = true;
                }
            }
            this.myAgent.doDelete();
        }
    }
    
    private void tratarQueryRef(ACLMessage msg){
        
        String content[] = msg.getContent().split(",");
        // Confirmar (agree)
        ACLMessage agree = msg.createReply();
        agree.setPerformative(ACLMessage.AGREE);
        this.myAgent.send(agree);
            
        // CASOS EN LOS QUE SE PUEDE RECIBIR UN QUERY_REF
        // ESTADO DEL SISTEMA
        if (content[0].equals(tipoMensaje.PEDIRINFORMACION.name())) {
            
            
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
        else if(content[0].equals(tipoMensaje.ATACAR.name())){
            // Determinar el bando de quién ha enviado el mensaje
            Agente a;
            if(content[1].equals(tipoAgente.SISTEMA.name())){
                // tomar agente random de la resistencia (agentesResistencia)
                a = elegirRandom(tipoAgente.RESISTENCIA);
                
            }
            else{
                // tomar agente random del sistema (agentesSistema)
                 a = elegirRandom(tipoAgente.SISTEMA);
            }
            
            /// Informs de arquitecto a agentes:
            // TIPOAGENTE, nombre
            ACLMessage inform_result = new ACLMessage(ACLMessage.INFORM_REF);
            inform_result.addReceiver(new AID(msg.getSender().getLocalName(), AID.ISLOCALNAME));
            inform_result.setContent(a.getTipoAgente() + "," + a.getNombre());
            this.myAgent.send(inform_result);
        }
        // RECLUTAR
        else{
            Agente a;
            // tomar agente JoePublic random (agentesJoePublic)
            a = elegirRandom(tipoAgente.JOEPUBLIC);
            
            // Enviar tipo agente y su nombre al que quiere reclutar
            ACLMessage inform_result = new ACLMessage(ACLMessage.INFORM_REF);
            inform_result.addReceiver(new AID(msg.getSender().getLocalName(), AID.ISLOCALNAME));
            if(a.getNombre().equals(tipoAgente.ORACULO.name())){
                inform_result.setContent(tipoAgente.ORACULO.name() + "," + a.getNombre());
            }
            else{
                inform_result.setContent(a.getTipoAgente().name() + "," + a.getNombre());
            }
            
            
            this.myAgent.send(inform_result);
            
        }
        
        
    }
    
    // Como resultado del combate o como resultado del reclutamiento
    private void tratarRequest(ACLMessage msg){
        String content[] = msg.getContent().split(",");
        
        if (content[0].equals(tipoMensaje.RESULTADO.name())) {
            // Confirmar (agree)
            ACLMessage agree = msg.createReply();
            agree.setPerformative(ACLMessage.AGREE);
            this.myAgent.send(agree);
            
            Evento e = null;
            //RESULT COMBATE
            if(content[2].equals(tipoAccion.COMBATE.name())){
                if(content[3].equals(tipoResultado.EXITO.name())){
                    if(content[1].equals(tipoAgente.RESISTENCIA.name())){
                        //el agente resistencia queda libre
                        Agente a = new Agente(msg.getSender().getLocalName(),tipoAgente.RESISTENCIA);
                        agentesResistenciaLibres.add(a);
                        
                        //ha matado a uno de sistema
                        Agente aS = new Agente(content[4], tipoAgente.SISTEMA);
                        agentesSistema.remove(aS);
                        
                        //registro el log
                        e = new Evento(agentesResistencia, agentesSistema, agentesJoePublic, tipoAccion.COMBATE, tipoResultado.EXITO);
                    }
                    else{
                        //gana combate agente Sistema
                        //el agente sistema queda libre
                        Agente a = new Agente(msg.getSender().getLocalName(),tipoAgente.SISTEMA);
                        agentesSistemaLibres.add(a);
                        
                        //ha matado a uno de resistencia
                        Agente aR = new Agente(content[4], tipoAgente.RESISTENCIA);
                        agentesSistema.remove(aR);
                        
                        //registro el log
                        e = new Evento(agentesResistencia, agentesSistema, agentesJoePublic, tipoAccion.COMBATE, tipoResultado.EXITO);
                    }
                }
                else if(content[3].equals(tipoResultado.FRACASO.name())){
                    if(content[1].equals(tipoAgente.RESISTENCIA.name())){
                        //pierde combate agente Resisencia
                        //el agente sistema queda libre
                        Agente a = new Agente(content[4],tipoAgente.SISTEMA);
                        agentesSistemaLibres.add(a);
                        
                        //han matado a uno de resistencia
                        Agente aR = new Agente(msg.getSender().getLocalName(), tipoAgente.RESISTENCIA);
                        agentesResistencia.remove(aR);
                        
                        //registro el log
                        e = new Evento(agentesResistencia, agentesSistema, agentesJoePublic, tipoAccion.COMBATE, tipoResultado.FRACASO);
                    }
                    else{
                        //pierde combate agente Sistema
                        //el agente resistencia queda libre
                        Agente a = new Agente(content[4],tipoAgente.RESISTENCIA);
                        agentesResistenciaLibres.add(a);
                        
                        //han matado a uno de sistema
                        Agente aS = new Agente(msg.getSender().getLocalName(), tipoAgente.SISTEMA);
                        agentesSistema.remove(aS);
                        
                        //registro el log
                        e = new Evento(agentesResistencia, agentesSistema, agentesJoePublic, tipoAccion.COMBATE, tipoResultado.FRACASO);
                    }
                }
                 else if(content[3].equals(tipoResultado.EMPATE.name())){
                     if(content[1].equals(tipoAgente.RESISTENCIA.name())){
                        //el agente sistema queda libre
                        Agente a = new Agente(content[4],tipoAgente.SISTEMA);
                        agentesSistemaLibres.add(a);
                        //el agente resistencia queda libre
                        Agente aR = new Agente(msg.getSender().getLocalName(), tipoAgente.RESISTENCIA);
                        agentesResistenciaLibres.add(aR);
                        
                        //registro el log
                        e = new Evento(agentesResistencia, agentesSistema, agentesJoePublic, tipoAccion.COMBATE, tipoResultado.EMPATE);                        
                     }
                     else{
                         //el agente sistema queda libre
                        Agente a = new Agente(msg.getSender().getLocalName(),tipoAgente.SISTEMA);
                        agentesSistemaLibres.add(a);
                        //el agente resistencia queda libre
                        Agente aR = new Agente(content[4], tipoAgente.RESISTENCIA);
                        agentesResistencia.remove(aR);
                        
                        //registro el log
                        e = new Evento(agentesResistencia, agentesSistema, agentesJoePublic, tipoAccion.COMBATE, tipoResultado.EMPATE);
                     }
                 }
            }
            //RESULT RECLUTAMIENTO
            else{
                if(content[3].equals(tipoResultado.EXITO.name())){
                    if(content[1].equals(tipoAgente.RESISTENCIA.name())){
                        //el agente resistencia queda libre
                        Agente aR = new Agente(msg.getSender().getLocalName(), tipoAgente.RESISTENCIA);
                        agentesResistenciaLibres.remove(aR);
                        //He reclutado a otro de resistencia
                        aR = new Agente(content[4], tipoAgente.RESISTENCIA);
                        agentesResistencia.add(aR);
                        agentesResistenciaLibres.add(aR);
                        //registro el log
                        e = new Evento(agentesResistencia, agentesSistema, agentesJoePublic, tipoAccion.RECLUTAMIENTO, tipoResultado.EXITO);
                    }
                    else if(content[1].equals(tipoAgente.SISTEMA.name())){
                        //el agente sistema queda libre
                        Agente aS = new Agente(msg.getSender().getLocalName(), tipoAgente.SISTEMA);
                        agentesSistemaLibres.remove(aS);
                        //He reclutado a otro de sistema
                        aS = new Agente(content[4], tipoAgente.SISTEMA);
                        agentesSistema.add(aS);
                        agentesSistemaLibres.add(aS);
                        //registro el log
                        e = new Evento(agentesResistencia, agentesSistema, agentesJoePublic, tipoAccion.RECLUTAMIENTO, tipoResultado.EXITO);
                    }
                    
                }
                else if(content[3].equals(tipoResultado.FRACASO.name())){
                    if(content[1].equals(tipoAgente.RESISTENCIA.name())){
                        //el agente resistencia queda libre
                        Agente aR = new Agente(msg.getSender().getLocalName(), tipoAgente.RESISTENCIA);
                        agentesResistenciaLibres.remove(aR);
                        //registro el log
                        e = new Evento(agentesResistencia, agentesSistema, agentesJoePublic, tipoAccion.RECLUTAMIENTO, tipoResultado.FRACASO);
                    }
                    else if(content[1].equals(tipoAgente.SISTEMA.name())){
                        //el agente sistema queda libre
                        Agente aS = new Agente(msg.getSender().getLocalName(), tipoAgente.SISTEMA);
                        agentesSistemaLibres.remove(aS);
                        //al no reclutarlo mata al joepublic
                        Agente aJ = new Agente(content[4], tipoAgente.JOEPUBLIC);
                        agentesJoePublic.remove(aJ);
                        //registro el log
                        e = new Evento(agentesResistencia, agentesSistema, agentesJoePublic, tipoAccion.RECLUTAMIENTO, tipoResultado.FRACASO);
                    }
                }
                else if(content[3].equals(tipoResultado.ORACULO.name())){
                    //se elimina oraculo de la lista de Joe Publics
                    Agente aJ = new Agente(content[4], tipoAgente.JOEPUBLIC);
                    agentesJoePublic.remove(aJ);
                    //registro el log
                    e = new Evento(agentesResistencia, agentesSistema, agentesJoePublic, tipoAccion.RECLUTAMIENTO, tipoResultado.ORACULO);
                }

            }
            //se mete el evento en el log (lista de eventos)
            log.add(e);
            
            // Inform request
            ACLMessage inform = msg.createReply();
            agree.setPerformative(ACLMessage.INFORM);
            this.myAgent.send(inform);
            
        }         
             
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
