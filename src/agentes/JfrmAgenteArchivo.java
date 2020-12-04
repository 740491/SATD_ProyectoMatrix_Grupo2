/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agentes;

/**
 *
 * @author Johnny Alexander
 */
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class JfrmAgenteArchivo extends javax.swing.JFrame {

    /**
     * Creates new form JfrmAgenteArchivo
     */
    GuiAgent owner;

    public JfrmAgenteArchivo(GuiAgent agente) {
        this.owner = agente;//recibe por parametro el agente que lo creo        
        initComponents();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        JpIndex = new javax.swing.JPanel();
        jbtnIniciar = new javax.swing.JButton();
        jbtnBrowser = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jlblRuta = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        JpIndex.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Algoritmos clasificacion + Agentes", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N
        JpIndex.setToolTipText("Click en el boton para iniciar");

        jbtnIniciar.setText("Iniciar proceso");
        jbtnIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnIniciarActionPerformed(evt);
            }
        });

        jbtnBrowser.setText("Seleccionar archivo");
        jbtnBrowser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnBrowserActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("Ruta:");

        javax.swing.GroupLayout JpIndexLayout = new javax.swing.GroupLayout(JpIndex);
        JpIndex.setLayout(JpIndexLayout);
        JpIndexLayout.setHorizontalGroup(
            JpIndexLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JpIndexLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JpIndexLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(JpIndexLayout.createSequentialGroup()
                        .addComponent(jbtnBrowser)
                        .addGap(18, 18, 18)
                        .addComponent(jbtnIniciar, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JpIndexLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlblRuta, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        JpIndexLayout.setVerticalGroup(
            JpIndexLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JpIndexLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JpIndexLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbtnBrowser)
                    .addComponent(jbtnIniciar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(JpIndexLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jlblRuta, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(JpIndex, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(JpIndex, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnIniciarActionPerformed
        GuiEvent evento = new GuiEvent(this, 0);//el cero indica la accion a realizar, el 0 es para enviar
        this.owner.postGuiEvent(evento);//se le indica al agente que se activo un evento
    }//GEN-LAST:event_jbtnIniciarActionPerformed

    private void jbtnBrowserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnBrowserActionPerformed
        String ruta;
        final JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos arff-csv", "arff", "csv");
        chooser.setFileFilter(filter);
        chooser.showOpenDialog(JpIndex);
        ruta = chooser.getSelectedFile().toString();
        jlblRuta.setText(ruta);
        System.out.println(ruta);

    }//GEN-LAST:event_jbtnBrowserActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel JpIndex;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton jbtnBrowser;
    private javax.swing.JButton jbtnIniciar;
    private javax.swing.JLabel jlblRuta;
    // End of variables declaration//GEN-END:variables

    public String obtenerRuta() {
        return jlblRuta.getText();
    }
}
