
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
    
    private static String[] RESISTENCIA_INIT = {"Neo","Morfeo","Triniti"};
    private static String[] SISTEMA_INIT = {"Smith","Torrente","Terminator"};
    
    enum tipoAgente{
        RESISTENCIA,
        SISTEMA,
        JOEPUBLIC
    }
    
    enum tipoAccion{
        ATACAR,
        RECLUTAR
    }
    
    enum tipoResultado{
        EXITO,
        EMPATE,
        FRACASO,
        ORACULO
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
    
    public class Evento{
        int num_evento;
        List<Agente> agentesResistencia;
        List<Agente> agentesSistema;
        List<Agente> agentesJoePublic;
        tipoAccion accion;
        tipoResultado resultado;
        
        private Evento(int num_evento, List<Agente> agentesResistencia, List<Agente> agentesSistema, List<Agente> agentesJoePublic, tipoAccion accion, tipoResultado resultado){
            this.num_evento = num_evento;
            this.agentesResistencia = agentesResistencia;
            this.agentesSistema = agentesSistema;
            this.agentesJoePublic = agentesJoePublic;
            this.accion = accion;
            this.resultado = resultado;
        }
    }
    
    public class Log{
        List<Evento> log;
        int num_eventos;
        
        private Log(List<Evento> log, int num_eventos){
            this.log = log;
            this.num_eventos = num_eventos;
        }
    }
    
    /*
        ATRIBUTOS ARQUITECTO
    */
    List<Agente> agentesResistencia;
    List<Agente> agentesSistema;
    List<Agente> agentesJoePublic;
    Log log;
    
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
        log = new Log(new ArrayList(), 0);
        
        // Dar valor a los agentes de La Resistencia
        for(String agente:RESISTENCIA_INIT) {
            Agente r = new Agente((String)agente, tipoAgente.RESISTENCIA, 50);
            agentesResistencia.add(r);
        }
        
        // Dar valor a los agentes del Sistema
        for(String agente:SISTEMA_INIT) {
            Agente s = new Agente((String)agente, tipoAgente.SISTEMA, 50);
            agentesResistencia.add(s);
        }
        
        // Dar valor a los agentes JoePublic, pasados como argumentos
        Object[] args = getArguments();
        //nos pasan en argumentos el nombre de todos los agentes joe public
        for(Object s : args ){
            Agente candidato = new Agente((String)s, tipoAgente.JOEPUBLIC, -1);

            agentesJoePublic.add(candidato);
        }
        
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
