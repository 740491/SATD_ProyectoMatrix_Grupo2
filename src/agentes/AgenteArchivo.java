
package agentes;


import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class AgenteArchivo extends GuiAgent {

    BufferedReader file = null;//Buffer para el archivo a leer
    private JfrmAgenteArchivo vfile;//objeto de la interfaz grafica

    /*Evento llamado desde la interfaz grafica*/
    @Override
    protected void onGuiEvent(GuiEvent ge) {

        ACLMessage msg = new ACLMessage(ACLMessage.CONFIRM);//se define objeto de tipo mensaje

        try {
            String ruta = vfile.obtenerRuta();
            System.out.println(ruta);
            file = new BufferedReader(new FileReader(ruta)); //Se lee el archivo
            msg.setContent(convertir(file));//se le añade el contenido al objeto de tipo mensaje, convirtiendo el Buffer en un String
            msg.addReceiver(new AID("dm", AID.ISLOCALNAME));//AID= Agent identification, se le añade a quien se le envia
            send(msg); //el agente actual envia el mensaje                
            file.close();//se cierra el archivo
        } catch (IOException ex) {
            Logger.getLogger(AgenteArchivo.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(vfile, ex.getMessage());
        }

    }
    /*
     * Convierte un elemento Buffer a un String y lo retorna
     */
    public String convertir(BufferedReader file) throws IOException {
        String temp;//Almacena la linea leida del file
        String cadena = "";//cadena final
        while ((temp = file.readLine()) != null) {
            cadena = cadena + temp + "\n";
        }
        return cadena;
    }

    /*Se definen los comportamientos del agente*/
    protected void setup() {
        vfile = new JfrmAgenteArchivo(this);//se instancia la interfaz
        vfile.setVisible(true);//se muestra la interfaz
 
    }
}
