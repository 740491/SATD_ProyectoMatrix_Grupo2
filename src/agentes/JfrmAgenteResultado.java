/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agentes;

import jade.gui.GuiAgent;
import java.awt.event.ActionEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Johnny Alexander
 */
public class JfrmAgenteResultado extends javax.swing.JFrame {

    /**
     * Creates new form JfrmAgenteResultado
     */
    GuiAgent owner;
    String[] eventos;
    String[] agentes;
    
    public JfrmAgenteResultado(GuiAgent agente) {
        this.owner = agente;//recibe por parametro el agente que lo creo        
        initComponents();
    }

    public void mostrarResultado(String str){		
        //Imprime el string
        //jtxtAreaEventos.setText(str);
        System.out.println("==TOTAL:"+str);
        String[] totalEventos = str.split(";,");
        System.out.println(totalEventos.length);
        eventos = new String[totalEventos.length];
        agentes = new String[totalEventos.length];
        int ind = 0;
                
        for (String evento : totalEventos) {
            System.out.println("==EV:"+evento);
            if(evento != "]") {
                String[] partes = evento.split("%");
                eventos[ind] = partes[0];
                System.out.println("==P0:"+partes[0]);
                agentes[ind] = partes[1]; //.replace('|', '\n'); 
                System.out.println("==P1:"+partes[1]);
                ind++;
            }
        }
        jListEventos.setListData(eventos);
        
        jListEventos.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                int index = jListEventos.getSelectedIndex();
        
                if(agentes.length > 0) {
                    jTxtAreaAgentes.setText(agentes[index].replace('|', '\n'));
                }
            }
        });
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTxtAreaAgentes = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        jListEventos = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Resultado : Lista de Eventos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

        jTxtAreaAgentes.setColumns(20);
        jTxtAreaAgentes.setRows(5);
        jScrollPane2.setViewportView(jTxtAreaAgentes);

        jListEventos.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Test" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
            public void setElementAt(int i, String str) { strings[i] = str; }
        });
        jScrollPane3.setViewportView(jListEventos);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 739, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.getAccessibleContext().setAccessibleName("Resultado : Lista de Eventos");

        pack();
    }// </editor-fold>                        

    
    // Variables declaration - do not modify                     
    private javax.swing.JList<String> jListEventos;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTxtAreaAgentes;
    // End of variables declaration                   
}