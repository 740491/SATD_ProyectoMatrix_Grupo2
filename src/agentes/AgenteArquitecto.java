
package agentes;

import agentes.MensajesComunes.*;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.ContainerController;
import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
//import javafx.util.Pair;
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

public class AgenteArquitecto extends Agent {
    
    private static String[] RESISTENCIA_INIT = {"Neo","Morfeo","Triniti"};
    private static String[] SISTEMA_INIT = {"Smith","Torrente","Terminator"};
    
    List<String> log;
        
    /*
        ATRIBUTOS ARQUITECTO
    */
    List<Agente> agentesResistencia;
    List<Agente> agentesSistema;
    List<Agente> agentesJoePublic;
    
    /*public class Recopilar_behaviour extends CyclicBehaviour {        

        @Override
        public void action() {
            
        }
    }*/
    
    /*Asignacion de comportamientos*/
    @Override
    protected void setup() {
        
        System.out.println("Arquitecto "+ getLocalName()+" creado");
        
        // Inicializar valores de las listas
        agentesResistencia = new ArrayList(); //inicialmente siempre los mismos
        agentesSistema = new ArrayList();
        agentesJoePublic = new ArrayList();
        
        // Inicializar y dar valor al log
        log = new ArrayList();
        
        // Dar valor a los agentes de La Resistencia
        for(String agente:RESISTENCIA_INIT) {
            Agente r = new Agente((String)agente, tipoAgente.RESISTENCIA);
            agentesResistencia.add(r);
        }
        
        // Dar valor a los agentes del Sistema
        for(String agente:SISTEMA_INIT) {
            Agente s = new Agente((String)agente, tipoAgente.SISTEMA);
            agentesSistema.add(s);
        }
        
        // Dar valor a los agentes JoePublic, pasados como argumentos
        Object[] args = getArguments();
        //nos pasan en argumentos el nombre de todos los agentes joe public
        for(Object s : args ){
            Agente candidato = new Agente((String)s, tipoAgente.JOEPUBLIC);

            agentesJoePublic.add(candidato);
        }
        
        //MessageTemplate protocolo = MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_QUERY);// Crear un MessageTemplate de tipo FIPA_REQUEST;
        //MessageTemplate performativa = MessageTemplate.MatchPerformative(ACLMessage.QUERY_REF);// Asignar una Performativa de tipo REQUEST al objeto MessageTemplate
        //MessageTemplate plantilla = MessageTemplate.and(protocolo,performativa); //Componer Plantilla con las anteriores
        ContainerController cc = getContainerController();
        this.addBehaviour(new AgenteArquitectoManejador( agentesResistencia, agentesSistema, agentesJoePublic, log, cc));
    }
    
    @Override
    protected void takeDown() {
        System.out.println("El agente " + getLocalName() + " muere");
    }
}
