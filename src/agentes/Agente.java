/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agentes;

import agentes.MensajesComunes.tipoAgente;

/**
 *
 * @author nacho
 */
public class Agente {

    tipoAgente tipo;
    String nombre;

    public Agente(String nombre, tipoAgente tipoAgente) {
        this.nombre = nombre;
        this.tipo = tipoAgente;
    }
    public String getNombre(){
        return this.nombre;
    }
    public tipoAgente getTipoAgente(){
        return this.tipo;
    }

    @Override
    public String toString() {
        return "Agente{" + "nombre= " + nombre + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (this.tipo != null ? this.tipo.hashCode() : 0);
        hash = 29 * hash + (this.nombre != null ? this.nombre.hashCode() : 0);
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
        final Agente other = (Agente) obj;
        if ((this.nombre == null) ? (other.nombre != null) : !this.nombre.equals(other.nombre)) {
            return false;
        }
        if (this.tipo != other.tipo) {
            return false;
        }
        return true;
    }
    
    
}
