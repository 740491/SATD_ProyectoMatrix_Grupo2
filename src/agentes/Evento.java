/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agentes;

import java.util.List;

/**
 *
 * @author nacho
 */
public class Evento {
    List<Agente> agentesResistencia;
    List<Agente> agentesSistema;
    List<Agente> agentesJoePublic;
    MensajesComunes.tipoAccion accion;
    MensajesComunes.tipoResultado resultado;

    public Evento( List<Agente> agentesResistencia, List<Agente> agentesSistema, List<Agente> agentesJoePublic, MensajesComunes.tipoAccion accion, MensajesComunes.tipoResultado resultado){
        this.agentesResistencia = agentesResistencia;
        this.agentesSistema = agentesSistema;
        this.agentesJoePublic = agentesJoePublic;
        this.accion = accion;
        this.resultado = resultado;
    }      
}
