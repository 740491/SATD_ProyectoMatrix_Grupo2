
package agentes;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
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
    
    enum tipoAgente{
        RESISTENCIA,
        SISTEMA,
        JOEPUBLIC
    }
    
    public class Agente{
        tipoAgente tipo;
        String nombre;
        int bonus;

        private Agente(String nombre, tipoAgente tipoAgente, int i) {
            this.nombre = nombre;
            this.tipo = tipoAgente;
            this.bonus = i;
        }
    }
   
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
        
        agentesResistencia = new ArrayList(); //inicialmente siempre los mismos
        agentesSistema = new ArrayList();
        agentesJoePublic = new ArrayList();
        
        Object[] args = getArguments();
        //nos pasan en argumentos el nombre de todos los agentes joe public
        for(Object s : args ){
            Agente candidato = new Agente((String)s, tipoAgente.JOEPUBLIC, 50);

            agentesJoePublic.add(candidato);
        }
        /*
            args[0] = listaResistencia
            args[1] = listaSistema
            args[2] = listaJoePublic
        */
        
        //if (args!= null && args.length == 3){
        //    agentesResistencia
        //}
        
        //MessageTemplate protocolo = MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_QUERY);// Crear un MessageTemplate de tipo FIPA_REQUEST;
        //MessageTemplate performativa = MessageTemplate.MatchPerformative(ACLMessage.QUERY_REF);// Asignar una Performativa de tipo REQUEST al objeto MessageTemplate
        //MessageTemplate plantilla = MessageTemplate.and(protocolo,performativa); //Componer Plantilla con las anteriores
              
        this.addBehaviour(new AgenteArquitectoManejador());
    }
    
    @Override
    protected void takeDown() {
        System.out.println("El agente " + getLocalName() + " muere");
    }
    
}
