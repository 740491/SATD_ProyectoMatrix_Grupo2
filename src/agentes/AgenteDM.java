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
    
    private static final long TIMEOUT = 7500;

    private String modelo;
    private int porcentaje;

    public class aplicarClasificacion extends OneShotBehaviour {

        public void action() {
                     
            ACLMessage msg = this.myAgent.blockingReceive();//accede al agente que tenga la accion y lo bloquea para que solo quede esperando el mensaje        
            String mensaje = msg.getContent();//recibimos el mensaje
            StringReader sr = new StringReader(mensaje); // El mensaje tipo String lo convertimos a un StringReader
            BufferedReader br = new BufferedReader(sr); // el StringReader lo convertimos a un Buffer
            

            try {
                
                Instances data = new Instances(br);//definimos un objeto que contendra los datos a clasificar
                data.setClassIndex(data.numAttributes() - 1);//Seleccionamos cual sera el atributo clase
                //int seed = 1;
                Random rnd = new Random();
                data.randomize(rnd);  // revolvemos el data set antes de hacer el split
                int trainSize = (int) Math.round(data.numInstances()*porcentaje/100);
                int testSize = data.numInstances()-trainSize;
                Instances entrenamiento = new Instances (data, 0, trainSize);
                Instances test = new Instances (data, trainSize,testSize);
                br.close();//cerramos el objeto buffer         
                             
                /*
                resultado=
                *[0] = porcentaje particion, 
                * [1] = instancias clasificadas, 
                * [2] = %instancias correctamente clasificadas, 
                * [3] = numero instancias correctamente clasificadas, 
                * [4] = %instancias incorrectamente clasificadas, 
                * [5] = numero instancias incorrectamente clasificadas
                */
                ACLMessage agree = msg.createReply();
                agree.setPerformative(ACLMessage.AGREE);
                //agree.addReceiver(new AID(msg.getSender().getName(), AID.ISLOCALNAME));//AID= Agent identification, se le añade a quien se le envia, recopilador
                this.myAgent.send(agree);
                String resultado=porcentaje+",";//Obtenemos resultados
                if("J48".equals(modelo)){
                    long startJ48 =System.currentTimeMillis();
                    J48 j48 = new J48(); // Creamos un clasidicador J48
                    j48.buildClassifier(entrenamiento);//creamos el clasificador  del J48 con los datos 
                    Evaluation evalJ48 = new Evaluation(entrenamiento);//Creamos un objeto para la validacion del modelo con redBayesiana
                    /*Aplicamos el clasificador J48*/
                    evalJ48.crossValidateModel(j48, test, 10, new Random(1));//hacemos validacion cruzada con 10 campos, y el aleatorio
                    long endJ48 =System.currentTimeMillis();
                    resultado = resultado + ((int) evalJ48.numInstances() + ",");
                    resultado = resultado + (evalJ48.pctCorrect() + ",");
                    resultado = resultado + ((int) evalJ48.correct() + ",");
                    resultado = resultado + (evalJ48.pctIncorrect() + ",");
                    resultado = resultado + ((int) evalJ48.incorrect());
                    
                    System.out.println(this.myAgent.getLocalName() + " %Clasificado correctamente: " + evalJ48.pctCorrect() + " Tiempo" +
                            " de ejecución: "+ (endJ48-startJ48) + " ms" );
                    //System.out.println(this.myAgent.getLocalName() + " " + evalJ48.toMatrixString("Matrix"));
                }
                else if("NaiveBayes".equals(modelo)){
                    long startNaive =System.currentTimeMillis();
                    NaiveBayes naiveBayes = new NaiveBayes(); // Creamos un clasidicador Naive Bayes
                    naiveBayes.buildClassifier(entrenamiento);//creamos el clasificador  del Naive Bayes con los datos 
                    Evaluation evalNaiveBayes = new Evaluation(entrenamiento);//Creamos un objeto para la validacion del modelo con redBayesiana
                    /*Aplicamos el clasificador NaiveBayes*/
                    evalNaiveBayes.crossValidateModel(naiveBayes, test, 10, new Random(1));//hacemos validacion cruzada con 10 campos, y el aleatorio
                    long endNaive = System.currentTimeMillis();
                    resultado = resultado + ((int) evalNaiveBayes.numInstances() + ",");
                    resultado = resultado + (evalNaiveBayes.pctCorrect() + ",");
                    resultado = resultado + ((int) evalNaiveBayes.correct() + ",");
                    resultado = resultado + (evalNaiveBayes.pctIncorrect() + ",");
                    resultado = resultado + ((int) evalNaiveBayes.incorrect());
                    System.out.println(this.myAgent.getLocalName() + " %Clasificado correctamente: " + evalNaiveBayes.pctCorrect() + " Tiempo" +
                            " de ejecución: "+ (endNaive-startNaive) + " ms" );
                }
                else if("MultilayerPerceptron".equals(modelo)){
                    long startMultilayer =System.currentTimeMillis();
                    MultilayerPerceptron multilayerPerceptron = new MultilayerPerceptron(); // Creamos un clasidicador MultilayerPerceptron
                    multilayerPerceptron.buildClassifier(entrenamiento);//creamos el clasificador  del MultilayerPerceptron con los datos 
                    Evaluation evalMultilayerPerceptron = new Evaluation(entrenamiento);//Creamos un objeto para la validacion del modelo con MultilayerPerceptron
                    /*Aplicamos el clasificador NaiveBayes*/
                    evalMultilayerPerceptron.crossValidateModel(multilayerPerceptron, test, 10, new Random(1));//hacemos validacion cruzada con 10 campos, y el aleatorio    
                    long endMultilayer =System.currentTimeMillis();
                    resultado = resultado + ((int) evalMultilayerPerceptron.numInstances() + ",");
                    resultado = resultado + (evalMultilayerPerceptron.pctCorrect() + ",");
                    resultado = resultado + ((int) evalMultilayerPerceptron.correct() + ",");
                    resultado = resultado + (evalMultilayerPerceptron.pctIncorrect() + ",");
                    resultado = resultado + ((int) evalMultilayerPerceptron.incorrect());
                    System.out.println(this.myAgent.getLocalName() + " %Clasificado correctamente: " + evalMultilayerPerceptron.pctCorrect() + " Tiempo" +
                            " de ejecución: "+ (endMultilayer-startMultilayer) + " ms");
                }
                ACLMessage inform = msg.createReply();
                inform.setPerformative(ACLMessage.INFORM);
                this.myAgent.send(inform);
              
                ACLMessage mensaje_resultado = new ACLMessage(ACLMessage.REQUEST);//se define objeto de tipo mensaje
                mensaje_resultado.setContent(resultado);//se le añade el contenido al objeto de tipo mensaje
                mensaje_resultado.addReceiver(new AID("recopilador-" + modelo, AID.ISLOCALNAME));//AID= Agent identification, se le añade a quien se le envia, recopilador
                this.myAgent.send(mensaje_resultado); //el agente actual envia el mensaje
                
                //esperar agree de recopilador
                // while(not agree)
                boolean recopilador_agree = false;
                
                while(!recopilador_agree){
                    ACLMessage respuesta = this.myAgent.blockingReceive(TIMEOUT);
                    
                    // Si salta timeout -> respuesta = null
                    // Reenviar la petición (y no cambiar recopilador_agree)
                    if(respuesta == null){
                        this.myAgent.send(mensaje_resultado);
                        System.out.println("SALTA TIMEOUT-DM " + this.myAgent.getLocalName());
                    }
                    // Si la respuesta es un "agree" es lo que esperábamos
                    // Salir del bucle (recopilador_agree = true)
                    // Si no es un agree se ignora el mensaje
                    else if(ACLMessage.AGREE == respuesta.getPerformative() ){
                        recopilador_agree = true;
                        //System.out.println("RECIBIDO-DM "  + this.myAgent.getLocalName());
                    }
                    
                }
                // esperar inform
                boolean recopilador_inform = false;
                while(!recopilador_inform){
                    ACLMessage inform_respuesta = this.myAgent.blockingReceive(TIMEOUT);
                    
                    // Si salta timeout -> inform_respuesta = null
                    // Reenviar la petición (y no cambiar recopilador_inform)
                    if(inform_respuesta == null){
                        this.myAgent.send(mensaje_resultado);
                    }
                    
                    // Si la respuesta es un "inform" es lo que esperábamos
                    // Salir del bucle (recopilador_inform = true)
                    // Si no es un inform se ignora el mensaje
                    else if(ACLMessage.INFORM == inform_respuesta.getPerformative() ){
                        recopilador_inform = true;
                    }
                }

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
        Object[] args = getArguments();
        modelo = args[0].toString();
        porcentaje = Integer.parseInt(args[1].toString());
    }
}