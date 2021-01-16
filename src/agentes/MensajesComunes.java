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
    
    // Mensajes con arquitecto
    enum tipoMensaje{
        RESULTADO, //tipoAgente, tipoAccion, tipoResultado, nombreRival  (tipoAccion = ATACAR,RECLUTAR)
        PEDIRINFORMACION, //tipoAgente
        ATACAR, //tipoAgente, bonus
        RECLUTAR, //tipoAgente, bonus
    }
    /// Informs de arquitecto a agentes:
    // TIPOAGENTE, nombre
    // PEDIRINFORMACION, informacion    (respuesta)
    
    // Mensajes entre agentes / resultado a arquitecto: 
    enum tipoAccion{
        COMBATE,
        RECLUTAMIENTO,
        CONOCERORACULO
    }
    // REQUEST                           AGREE           INFORM
    // ATACAR, tipoAgente, bonus         ATACAR          ATACAR,tipoResultado
    
    // RECLUTAMIENTO,tipoAgente, bonus   RECLUTAMIENTO   RECLUTAMIENTO,tipoResultado
    // CONOCERORACULO,tipoAgente         CONOCERORACULO  CONOCERORACULO, ORACULO
    
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
