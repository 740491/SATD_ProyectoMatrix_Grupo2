package agentes;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.util.Arrays;

import static java.lang.System.exit;

public class AgenteInit extends Agent {
    private static int NUM_EXEC= 1;
    public class Iniciar_Agentes extends OneShotBehaviour{
        public void action(){
            ContainerController cc = getContainerController();
            AgentController ac = null;
            Object[] arguments=getArguments();
            if (arguments!= null && arguments.length>=1 && ! arguments[0].toString().equals("")){
                try {
                    AgenteInit.NUM_EXEC = Integer.parseInt(arguments[0].toString());
                }catch (Exception e){
                    System.err.println("ERROR: El argumento introducido no es un numero entero");
                    exit(1);
                }
            }
            try {
                    ac = cc.createNewAgent("joepublic", "agentes.AgenteJoePublic", null);
                    ac.start();
                    ac = cc.createNewAgent("oraculo", "agentes.Oraculo", null);
                    ac.start();
                    ac = cc.createNewAgent("resistencia", "agentes.AgenteResistencia", null);
                    ac.start();
                    ac = cc.createNewAgent("resultado", "agentes.AgenteResultado", null);
                    ac.start();
                    Object argumentos[]= new String[]{"arquitecto"};
                    ac = cc.createNewAgent("sistema", "agentes.AgenteSistema", argumentos);
                    ac.start();
                    ac = cc.createNewAgent("arquitecto", "agentes.AgenteArquitecto", null);
                    ac.start();
            } catch (StaleProxyException e) {
                e.printStackTrace();
            }
            doDelete();
        }
    }
    /*Asignacion de comportamientos*/
    protected void setup() {
        AgenteInit.Iniciar_Agentes cs = new AgenteInit.Iniciar_Agentes();
        this.addBehaviour(cs);
    }
}
