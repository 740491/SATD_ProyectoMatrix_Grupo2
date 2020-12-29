package agentes;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.util.Arrays;

import static java.lang.System.exit;

public class AgenteInit extends Agent {
    private static int NUM_EXEC= 5;
    private static final String[] modelos ={"J48","NaiveBayes","MultilayerPerceptron"};
    private static final int[] porcentajes ={30,50,80};
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
                for (String cad:AgenteInit.modelos){
                    ac = cc.createNewAgent("resultado-" + cad, "agentes.AgenteResultado", null);
                    ac.start();
                    Object argumentos[]= new String[]{cad, String.valueOf(AgenteInit.NUM_EXEC*AgenteInit.modelos.length)};
                    ac = cc.createNewAgent("recopilador-" + cad, "agentes.AgenteRecopilador", argumentos);
                    ac.start();
                }
                for (int i=0; i< AgenteInit.NUM_EXEC;i++){
                    for (String cad:AgenteInit.modelos){
                        for (int porcentaje_modelo:AgenteInit.porcentajes){
                            Object argumentos[]= new String[]{cad, String.valueOf(porcentaje_modelo)};
                            ac = cc.createNewAgent("dm-" + cad + "-" + porcentaje_modelo+ "-" + (i+1), "agentes.AgenteDM", argumentos);
                            ac.start();
                        }
                    }
                }
                Object[] argumentos = new String[]{Integer.toString(AgenteInit.NUM_EXEC)};
                ac = cc.createNewAgent("archivo", "agentes.AgenteArchivo", argumentos);
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
