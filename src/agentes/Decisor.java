/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agentes;

import java.util.Random;

import agentes.MensajesComunes.tipoAccion;

public class Decisor {
    public enum Estrategias{
        ALEATORIA,
        ESTRATEGIA1,
        ESTRATEGIA2,
        ESTRATEGIA3,
    }
    
    Estrategias estrategia;
    // Aqui las variables que hagan falta para almacenar la informacion
    
    public Decisor(Estrategias estrategia){
        this.estrategia = estrategia;
    }
    
    //Parsea y almacena la nueva información
    public void actualizar_info(String info){
        
    }
    
    // Solicitar combate con otro agente
    public tipoAccion decidir_accion(){
        tipoAccion resultado;
        switch(estrategia){
            case ESTRATEGIA1:
                resultado = estrategia1();
                break;
            case ESTRATEGIA2:
                resultado = estrategia2();
                break;
            case ESTRATEGIA3:
                resultado = estrategia3();
                break;
            default:
                resultado = estrategia_aleatoria();
                break;
        }
        return resultado;
    }
    
    public tipoAccion estrategia_aleatoria(){
        Random rand = new Random();
        float prob = rand.nextFloat();
        if(prob > 0.5){
            return tipoAccion.ATACAR;
        }else{
            return tipoAccion.RECLUTAR;
        }
    }
    
    // TODO
    public tipoAccion estrategia1(){
        Random rand = new Random();
        float prob = rand.nextFloat();
        if(prob > 0.5){
            return tipoAccion.ATACAR;
        }else{
            return tipoAccion.RECLUTAR;
        }
    }
    
    // TODO
    public tipoAccion estrategia2(){
        Random rand = new Random();
        float prob = rand.nextFloat();
        if(prob > 0.5){
            return tipoAccion.ATACAR;
        }else{
            return tipoAccion.RECLUTAR;
        }
    }
    
    // TODO
    public tipoAccion estrategia3(){
        Random rand = new Random();
        float prob = rand.nextFloat();
        if(prob > 0.5){
            return tipoAccion.ATACAR;
        }else{
            return tipoAccion.RECLUTAR;
        }
    }
    
}
