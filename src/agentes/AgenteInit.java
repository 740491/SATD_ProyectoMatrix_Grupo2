package agentes;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.System.exit;

public class AgenteInit extends Agent {
    private static int NUM_EXEC= 1;
    private static String[] RESISTENCIA_INIT = {"Neo","Morfeo","Triniti"};
    private static String[] SISTEMA_INIT = {"Smith","Torrente","Terminator"};
    private ArrayList<String> agentes_JP = new ArrayList<>();
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
                for(int i=0; i<NUM_EXEC;i++) {
                    ac = cc.createNewAgent("joepublic-"+ i, "agentes.AgenteJoePublic", null);
                    ac.start();
                    agentes_JP.add("joepublic-"+ i);
                }
                    ac = cc.createNewAgent("oraculo", "agentes.Oraculo", null);
                    ac.start();
                    for(String agente:RESISTENCIA_INIT) {
                        ac = cc.createNewAgent(agente, "agentes.AgenteResistencia", null);
                        ac.start();
                    }
                    ac = cc.createNewAgent("resultado", "agentes.AgenteResultado", null);
                    ac.start();
                    Object argumentos[]= new String[]{"arquitecto"};
                    for(String agente:SISTEMA_INIT) {
                        ac = cc.createNewAgent(agente, "agentes.AgenteSistema", argumentos);
                        ac.start();
                    }
                    argumentos= new String[]{};
                    argumentos=agentes_JP.toArray(argumentos);
                    ac = cc.createNewAgent("arquitecto", "agentes.AgenteArquitecto", argumentos);
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
