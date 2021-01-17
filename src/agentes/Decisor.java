/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agentes;

import java.util.Random;


public class Decisor {
    
    int probReclutar = 90;

    public enum Estrategias{
        ALEATORIA,
        ESTRATEGIA1,
        ESTRATEGIA2,
        ESTRATEGIA3,
        ATACAR,
        RECLUTAR,
        DESCENDENTE
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
        
        if(!info.equals("")){
            String content[] = info.split(",");
            
            try{
                if(content[0].equals(MensajesComunes.tipoMensaje.PEDIRINFORMACION.name())){
                    // PEDIRINFORMACION,agentesJoePublic.size(),agentesResistencia.size(),agentesSistema.size(),(NO)QUEDAORACULO
                    elegirEstrategiaCompleja(Integer.parseInt(content[1]), Integer.parseInt(content[2]), Integer.parseInt(content[3]), content[4]);
                }
                else{
                    // YA TRATADO
                }
            }
            catch(Exception e){
                System.out.println("SPLIT EN LA INFO DEL ESTADO INCORRECTO" + e);
            }
        }
        else{
            System.out.println("LA INFO DEL ESTADO NO ESTÁ BIEN");
        }
    }
    
    // ELEGIR ESTRATEGIA A PARTIR DEL ESTADO
    // PEDIRINFORMACION,agentesJoePublic.size(),agentesResistencia.size(),agentesSistema.size(),(NO)QUEDAORACULO
    public void elegirEstrategiaCompleja(int tamJoePublic, int tamResistencia, int tamSistema, String hayOraculo){
        System.out.println("ELIGIENDO ESTRATEGIA:");
        if(hayOraculo.equals("QUEDAORACULO")){
            this.estrategia = Estrategias.RECLUTAR;
            System.out.println("ESTRATEGIA: " + Estrategias.RECLUTAR.name());
        }
        else{
            this.estrategia = Estrategias.DESCENDENTE;
            System.out.println("ESTRATEGIA: " + Estrategias.DESCENDENTE.name());
        }
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
            case RECLUTAR:
                resultado = reclutar();
                break;
            case DESCENDENTE:
                resultado = descendente();
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
    
    // SE DECIDE RECLUTAR SIEMPRE
    public tipoDecision reclutar(){
        return tipoDecision.RECLUTAMIENTO;
    }
    
    // SE DECIDE ATACAR SIEMPRE
    public tipoDecision descendente(){
        this.probReclutar -= 5;
        
        Random rand = new Random();
        float prob = rand.nextFloat();
        
        if(prob*100 > probReclutar){
            return tipoDecision.COMBATE;
        }else{
            return tipoDecision.RECLUTAMIENTO;
        }
    }
    
}
