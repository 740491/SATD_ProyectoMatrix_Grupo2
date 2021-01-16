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
}
