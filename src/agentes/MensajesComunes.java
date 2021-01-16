/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agentes;

/**
 *
 * @author Tisho
 */
public class MensajesComunes {
    
    // tipoMensaje, tipoAgente, ...
    
    // RESULTADO, tipoAgente, tipoAccion, tipoResultado
    
    enum tipoMensaje{
        // Arquitecto - Agentes
        RESULTADO, //tipoAgente, tipoAccion, tipoResultado
        PEDIRINFORMACION, //tipoAgente
        ATACAR, //tipoAgente, bonus
        RECLUTAR, //tipoAgente, bonus
        CONOCERORACULO
    }
    
    /// Informs de arquitecto a agentes:
    // TIPOAGENTE, nombre
    // PEDIRINFORMACION, informacion    (respuesta)
    
    // Se utiliza para indicar EMISOR
    enum tipoAgente{
        RESISTENCIA,
        SISTEMA,
        JOEPUBLIC,
        ORACULO
    }
    
    enum tipoResultado{
        EXITO,
        EMPATE,
        FRACASO,
        ORACULO 
    }
    
    
    
    
    
    
    
    
    
    
    
   
    
    
    
}
