
package agentes;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import javafx.util.Pair;
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

public class AgenteRecopilador extends Agent {
    int contador= 4;
    String modelo;
    Integer maxMensajesPorModelo;
    String resRecopilador = "";
    String decisionTreeName = "DecisionTree";
    String naiveBayesName = "NaiveBayes";
    String multyplayerPerceptrionName = "MultyplayerPerceptrion";
    
    /***
    * keys del HashMap: DecisionThree, NaiveBayes, MultyplayerPerceptrion  -- rapresentan los modelos
    * cada modelo tiene un ArrayList:
    *
    * [0] = porcentaje particion, 
    * [1] = instancias clasificadas, 
    * [2] = %instancias correctamente clasificadas, 
    * [3] = numero instancias correctamente clasificadas, 
    * [4] = %instancias incorrectamente clasificadas, 
    * [5] = numero instancias incorrectamente clasificadas, 
    * 
    * para hacer la media, en cada posicion tendremos una copia:
    * - numero resultados recibidos
    * - suma de los resultados
    * 
    * ejemplo:
    * "DecisionThree" -> datosRecibidos.get(0) == porcentajeParticion.  
    * porcentajeParticion[0] = 6 (hemos recibido 6 porcentajes)
    * porcentajeParticion[1] = 60 (la suna de los porcentajeParticion es 60)
    * mediaPorcentajeParticion = 60/6 = 10
    * 
    * 
    * ...string matrix
     */
    HashMap<String, ArrayList<ArrayList<Integer>>> datosRecibidos = new HashMap();
    HashMap<String, ArrayList<Double>> mediasFinalesModelos = new HashMap();
   
    private void inicializaDatosModelos(){
        ArrayList<ArrayList<Integer>> arrModelo = new ArrayList();
        for(int i=0; i<6; i++){
            ArrayList<Integer> copia = new ArrayList();
            copia.add(0);
            copia.add(0);
            arrModelo.add(copia);
        }
        datosRecibidos.put("DecisionTree", arrModelo);
        datosRecibidos.put("NaiveBayes", arrModelo);
        datosRecibidos.put("MultyplayerPerceptrion", arrModelo);
    }
    
    public class Recopilar_behaviour extends CyclicBehaviour {
        
        
        //Envía resultados al agente resultado correspondiente
        private void enviar_resultados(String modelo) {
                //FORMATO RESULTADO:
                String resultadoRecopiladorPorModelo = modelo+",";  //modeloString,
                resultadoRecopiladorPorModelo += mediasFinalesModelos.get(modelo).get(0)+","; //mediaPorcentejeParticion,
                resultadoRecopiladorPorModelo += mediasFinalesModelos.get(modelo).get(1)+","; //media%instanciasClasificadas,
                resultadoRecopiladorPorModelo += mediasFinalesModelos.get(modelo).get(2)+","; //media%instanciasCorrectamenteClasificadas,
                resultadoRecopiladorPorModelo += mediasFinalesModelos.get(modelo).get(3)+","; //mediaNumeroInstanciasCorrectClasificadas,
                resultadoRecopiladorPorModelo += mediasFinalesModelos.get(modelo).get(4)+","; //media %instancias incorrectamente clasificadas,
                resultadoRecopiladorPorModelo += mediasFinalesModelos.get(modelo).get(5);     //mediaNumeroInstanciasIncorrectamenteClasificadas
              
                ACLMessage resultado = new ACLMessage(ACLMessage.CONFIRM);//se define objeto de tipo mensaje
                resultado.setContent(resultadoRecopiladorPorModelo);//se le añade el contenido al objeto de tipo mensaje
                resultado.addReceiver(new AID("????????????", AID.ISLOCALNAME));//AID= Agent identification, se le añade a quien se le envia
                this.myAgent.send(resultado); //el agente actual envia el mensaje
        }

        public void action() {
            ACLMessage msg = this.myAgent.blockingReceive();//accede al agente que tenga la accion y lo bloquea para que solo quede esperando el mensaje
            String mensaje = msg.getContent();//recibimos el mensaje
            
            //Calcular estadísticas
           
            anadeDatosModelo(modelo, mensaje);
            // Envíar ok a Agente DM
            
            if(datosRecibidos.get("DecisionTree").get(0).get(0) >= maxMensajesPorModelo){  //si ha recibido todos los mensajes para el DecisionTree -> envia
                calculaMediaModelo("DecisionTree");
                enviar_resultados("DecisionTree");
                contador-=1;
            }
            else if(datosRecibidos.get("NaiveBayes").get(0).get(0) >= maxMensajesPorModelo){  //si ha recibido todos los mensajes para el DecisionTree -> envia
                calculaMediaModelo("NaiveBayes");
                enviar_resultados("NaiveBayes");
                contador-=1;
            }   
             else if(datosRecibidos.get("MultyplayerPerceptrion").get(0).get(0) >= maxMensajesPorModelo){  //si ha recibido todos los mensajes para el DecisionTree -> envia
                calculaMediaModelo("MultyplayerPerceptrion");
                enviar_resultados("MultyplayerPerceptrion");
                contador-=1;
            }   
            
            if(contador == 0){
                myAgent.doDelete();
            }
        }
    }
    /*Asignacion de comportamientos*/
    protected void setup() {
        
        if(contador == 4){                  //la primera vez inicializa la estructura de datos
            this.inicializaDatosModelos();
            contador-=1;
        }
        Object[] args = getArguments();
        modelo = args[0].toString();
        maxMensajesPorModelo = Integer.parseInt(args[1].toString());
              
        this.addBehaviour(new Recopilar_behaviour());
    }
    private synchronized void anadeDatosModelo(String modelo, String mensaje) {
        String[] parametros = mensaje.split(",");
        
        for(int i=0; i<6; i++){  // 6 parametros por los cuales tenemos que hacer la media
            //example "DecisionThree" -> 
            int occurrences = datosRecibidos.get(modelo).get(i).get(0);
            int suma = datosRecibidos.get(modelo).get(i).get(1);
            datosRecibidos.get(modelo).get(i).set(0, occurrences + 1 );
            datosRecibidos.get(modelo).get(i).set(1, suma + Integer.parseInt(parametros[i]));
        }
        String stringMatrix = parametros[6];

    }
    private void calculaMediaModelo(String modelo){
        ArrayList<Double> mediasModelo = new ArrayList();  //cada posicion tiene la media de un parametro  - medias Finales para un modelo
        for(int i=0; i<6; i++){  // 6 parametros por los cuales tenemos que hacer la media
            int occurrences = datosRecibidos.get(modelo).get(i).get(0);
            double suma = datosRecibidos.get(modelo).get(i).get(1);
            
            mediasModelo.set(i, suma/occurrences);
            //modelo, mediaPorcentajeParticion, mediaInstanciasClasificadas, media%instanciasCorrectamenteClasificadas, numeroInstanciasCorrectamenteClasificadas, 
            //media%instanciasIncorrectamenteClasificadas, mediaInstanciasIncorrectamenteClasificadas 
        }
        mediasFinalesModelos.put(modelo, mediasModelo);
    }
}
