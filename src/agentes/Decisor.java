/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agentes;

import java.util.Random;


public class Decisor {

    public enum Estrategias{
        ALEATORIA,
        ESTRATEGIA1,
        ESTRATEGIA2,
        ESTRATEGIA3,
        ATACAR
    }
    
    // Acciones que deciden hacer los AgentesSistema, y Resistencia
    enum tipoDecision{
        COMBATE,
        RECLUTAMIENTO, //Incluye encontrar oráculo
        PEDIRINFORMACION
    }
    
    Estrategias estrategia;
    // Aqui las variables que hagan falta para almacenar la informacion:
    boolean hay_joepublic;
    
    public Decisor(Estrategias estrategia){
        this.estrategia = estrategia;
    }
    
    //Parsea y almacena la nueva información
    public void actualizar_info(String info){
        
    }
    
    // Solicitar combate con otro agente
    public tipoDecision decidir_accion(){
        tipoDecision resultado;
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
            case ATACAR:
                resultado = atacar();
                break;
            default:
                resultado = estrategia_aleatoria();
                break;
        }
        return resultado;
    }
    
    public tipoDecision estrategia_aleatoria(){
        Random rand = new Random();
        float prob = rand.nextFloat();
        if(prob > 0.6){
            return tipoDecision.COMBATE;
        }else{
            return tipoDecision.RECLUTAMIENTO;
        }
    }
    
    // TODO
    public tipoDecision estrategia1(){
        Random rand = new Random();
        float prob = rand.nextFloat();
        if(prob > 0.5){
            return tipoDecision.COMBATE;
        }else{
            return tipoDecision.RECLUTAMIENTO;
        }
    }
    
    // TODO
    public tipoDecision estrategia2(){
        Random rand = new Random();
        float prob = rand.nextFloat();
        if(prob > 0.5){
            return tipoDecision.COMBATE;
        }else{
            return tipoDecision.RECLUTAMIENTO;
        }
    }
    
    // TODO
    public tipoDecision estrategia3(){
        Random rand = new Random();
        float prob = rand.nextFloat();
        if(prob > 0.5){
            return tipoDecision.COMBATE;
        }else{
            return tipoDecision.RECLUTAMIENTO;
        }
    }
    
    // SE DECIDE ATACAR SIEMPRE
    public tipoDecision atacar(){
        return tipoDecision.COMBATE;
    }
    
}
