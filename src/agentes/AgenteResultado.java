
package agentes;

import jade.core.behaviours.OneShotBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;


public class AgenteResultado extends GuiAgent {
    long tiempoInicio;
    long tiempoFin;
    private JfrmAgenteResultado jfrm;//interfaz
    AgenteResultado ag = this;//variable que se almacena el mismo para ser accedido por la accion del agente
    
    @Override
    protected void onGuiEvent(GuiEvent ge) {
    }

    public class mostrarResultado extends OneShotBehaviour {

        public void action() {
            tiempoInicio = System.currentTimeMillis();
            ACLMessage msg = this.myAgent.blockingReceive();//accede al agente que tenga la accion y lo bloquea para que solo quede esperando el mensaje                  
               
            boolean recopilador_request = false;
            while(!recopilador_request){
                if(ACLMessage.REQUEST == msg.getPerformative() ){
                    recopilador_request = true;
                }
                else{
                    msg = this.myAgent.blockingReceive();
                }
                
            }
			
            ACLMessage agree = msg.createReply();
            agree.setPerformative(ACLMessage.AGREE);
            this.myAgent.send(agree);
			
            jfrm.setVisible(true);//solo se muestra la interfaz cuando se ha recibo el mensaje
            String mensaje = msg.getContent();//Obtenemos el resultado
            tiempoFin = System.currentTimeMillis();
            jfrm.mostrarResultado(mensaje, tiempoFin - tiempoInicio); //se llama la funcion mostrar resultado que se encuentra en el Jfrm
            
            ACLMessage inform = msg.createReply();
            inform.setPerformative(ACLMessage.INFORM);
            this.myAgent.send(inform);
        }
    }

    /*Asignacion de comportamientos*/
    protected void setup() {
        jfrm = new JfrmAgenteResultado(ag);//se instancia la interfaz
        mostrarResultado cs = new mostrarResultado();
        this.addBehaviour(cs);
    }
}
