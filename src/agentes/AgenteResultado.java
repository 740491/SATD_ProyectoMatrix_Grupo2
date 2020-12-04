
package agentes;

import jade.core.behaviours.OneShotBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;


public class AgenteResultado extends GuiAgent {

    private JfrmAgenteResultado vres;//interfaz
    AgenteResultado ag = this;//variable que se almacena el mismo para ser accedido por la accion del agente
    
    @Override
    protected void onGuiEvent(GuiEvent ge) {
    }

    public class mostrarResultado extends OneShotBehaviour {

        public void action() {
            ACLMessage msg = this.myAgent.blockingReceive();//accede al agente que tenga la accion y lo bloquea para que solo quede esperando el mensaje                  
            vres.setVisible(true);//solo se muestra la interfaz cuando se ha recibo el mensaje
            String mensaje = msg.getContent();//Obtenemos el resultado
            vres.mostrarResultado(mensaje); //se llama la funcion mostrar resultado que se encuentra en el Jfrm
        }
    }

    /*Asignacion de comportamientos*/
    protected void setup() {
        vres = new JfrmAgenteResultado(ag);//se instancia la interfaz
        mostrarResultado cs = new mostrarResultado();
        this.addBehaviour(cs);
    }
}
