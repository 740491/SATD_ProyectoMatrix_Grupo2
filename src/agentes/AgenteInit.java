package agentes;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class AgenteInit extends Agent {
    private static int NUM_EXEC= 5;
    public class Iniciar_Agentes extends OneShotBehaviour{
        public void action(){
            ContainerController cc = getContainerController();
            AgentController ac = null;
            int iteraciones = AgenteInit.NUM_EXEC;
            try {
                ac = cc.createNewAgent("pantalla", "agentes.AgenteResultado", null);
                ac.start();
                ac = cc.createNewAgent("dm", "agentes.AgenteDM", null);
                ac.start();
                ac = cc.createNewAgent("archivo", "agentes.AgenteArchivo", null);
                ac.start();
            } catch (StaleProxyException e) {
                e.printStackTrace();
            }
        }
    }
    /*Asignacion de comportamientos*/
    protected void setup() {
        AgenteInit.Iniciar_Agentes cs = new AgenteInit.Iniciar_Agentes();
        this.addBehaviour(cs);
    }
}
