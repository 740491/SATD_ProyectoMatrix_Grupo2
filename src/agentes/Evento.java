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
    String agenteQueEnvia;
    String agenteConQuienInteracciona;

    public Evento( List<Agente> agentesResistencia, List<Agente> agentesSistema, List<Agente> agentesJoePublic, MensajesComunes.tipoAccion accion, MensajesComunes.tipoResultado resultado, String agenteQueEnvia, String agenteConQuienInteracciona){
        this.agentesResistencia = agentesResistencia;
        this.agentesSistema = agentesSistema;
        this.agentesJoePublic = agentesJoePublic;
        this.accion = accion;
        this.resultado = resultado;
        this.agenteQueEnvia = agenteQueEnvia;
        this.agenteConQuienInteracciona = agenteConQuienInteracciona;

    }

    @Override
    public String toString() {
        return "Evento{" + "accion="+ accion + ", agenteQueEnvia="+ agenteQueEnvia + ",agenteConQuienInteracciona=" + agenteConQuienInteracciona + ", resultado=" + resultado+", agentesResistencia=" + agentesResistencia + ", agentesSistema=" + agentesSistema + ", agentesJoePublic=" + agentesJoePublic + '}';
    }

    

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.agentesResistencia != null ? this.agentesResistencia.hashCode() : 0);
        hash = 97 * hash + (this.agentesSistema != null ? this.agentesSistema.hashCode() : 0);
        hash = 97 * hash + (this.agentesJoePublic != null ? this.agentesJoePublic.hashCode() : 0);
        hash = 97 * hash + (this.accion != null ? this.accion.hashCode() : 0);
        hash = 97 * hash + (this.resultado != null ? this.resultado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Evento other = (Evento) obj;
        if (this.agentesResistencia != other.agentesResistencia && (this.agentesResistencia == null || !this.agentesResistencia.equals(other.agentesResistencia))) {
            return false;
        }
        if (this.agentesSistema != other.agentesSistema && (this.agentesSistema == null || !this.agentesSistema.equals(other.agentesSistema))) {
            return false;
        }
        if (this.agentesJoePublic != other.agentesJoePublic && (this.agentesJoePublic == null || !this.agentesJoePublic.equals(other.agentesJoePublic))) {
            return false;
        }
        if (this.accion != other.accion) {
            return false;
        }
        if (this.resultado != other.resultado) {
            return false;
        }
        return true;
    }
    
    
    
    
}
