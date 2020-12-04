
package agentes;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import weka.associations.Apriori;
import weka.associations.Associator;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;


public class AgenteDM extends Agent {

    public class aplicarClasificacion extends OneShotBehaviour {

        public void action() {
            ACLMessage msg = this.myAgent.blockingReceive();//accede al agente que tenga la accion y lo bloquea para que solo quede esperando el mensaje        
            String mensaje = msg.getContent();//recibimos el mensaje
            StringReader sr = new StringReader(mensaje); // El mensaje tipo String lo convertimos a un StringReader
            BufferedReader br = new BufferedReader(sr); // el StringReader lo convertimos a un Buffer

            try {
                Instances data = new Instances(br);//definimos un objeto que contendra los datos a clasificar
                data.setClassIndex(data.numAttributes() - 1);//Seleccionamos cual sera el atributo clase
                br.close();//cerramos el objeto buffer             
                J48 j48 = new J48(); // Creamos un clasidicador J48
                j48.buildClassifier(data);//creamos el clasificador  del J48 con los datos 
                Evaluation evalJ48 = new Evaluation(data);//Creamos un objeto para la validacion del modelo con redBayesiana
                /*Aplicamos el clasificador J48*/
                evalJ48.crossValidateModel(j48, data, 10, new Random(1));//hacemos validacion cruzada con 10 campos, y el aleatorio                 
                String resJ48 = "\nResultados Arbol de decision J48\n========\n";//Obtenemos resultados

                resJ48 = resJ48 + ("# de instancias clasificadas " + (int) evalJ48.numInstances() + "\n");
                resJ48 = resJ48 + ("% instancias correctamente clasificadas " + evalJ48.pctCorrect() + "\n");
                resJ48 = resJ48 + ("# instancias correctamente clasificadas " + (int) evalJ48.correct() + "\n");
                resJ48 = resJ48 + ("% instancias incorrectamente clasificadas " + evalJ48.pctIncorrect() + "\n");
                resJ48 = resJ48 + ("# instancias incorrectamente clasificadas " + (int) evalJ48.incorrect() + "\n");
                resJ48 = resJ48 + ("Media del error absoluto " + evalJ48.meanAbsoluteError() + "\n");
                resJ48 = resJ48 + (evalJ48.toMatrixString("Matrix de confusion"));

              // Se grafica el arbol
                final javax.swing.JFrame jf = new javax.swing.JFrame("Arbol de decision: J48");
                jf.setSize(500, 400);
                jf.getContentPane().setLayout(new BorderLayout());
                TreeVisualizer tv = new TreeVisualizer(null, j48.graph(), new PlaceNode2());
                jf.getContentPane().add(tv, BorderLayout.CENTER);
                jf.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        jf.dispose();
                    }
                });

                jf.setVisible(true);
                tv.fitToScreen();
                ACLMessage resultado = new ACLMessage(ACLMessage.CONFIRM);//se define objeto de tipo mensaje
                resultado.setContent(resJ48);//se le añade el contenido al objeto de tipo mensaje
                resultado.addReceiver(new AID("pantalla", AID.ISLOCALNAME));//AID= Agent identification, se le añade a quien se le envia
                this.myAgent.send(resultado); //el agente actual envia el mensaje

            } catch (Exception e) {
                System.out.println("El error es" + e.getMessage());
                JFrame MiVentana = new JFrame("Error"); //llamamos a la clase y creamos un objeto llamado MiVentana 
                JOptionPane.showMessageDialog(MiVentana, e.getMessage());
            }
        }
    }

    /*Asignacion de comportamientos*/
    protected void setup() {
        aplicarClasificacion cs = new aplicarClasificacion();
        this.addBehaviour(cs);
    }
}
