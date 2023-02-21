package com.fannog.cliente.ui.analista;

import com.fannog.cliente.utils.BeanFactory;
import com.fannog.servidor.DAO.TipoConstanciaDAO;
import com.fannog.servidor.entities.TipoConstancia;
import com.fannog.servidor.exceptions.ServicioException;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class CreateOrEditTipoConstancia extends javax.swing.JDialog {

    private TipoConstancia tipo;
    private File selectedFile;
    private final TiposConstancia tiposConstanciaPnl;

    public CreateOrEditTipoConstancia(java.awt.Frame parent, boolean modal, TipoConstancia tipo, TiposConstancia tiposConstanciaPnl) {
        super(parent, modal);
        this.tipo = tipo;
        this.tiposConstanciaPnl = tiposConstanciaPnl;

        initComponents();

        myInit();
    }

    private void myInit() {
        if (tipo == null) {
            btnCreateOrEdit.setText("Crear");
            setTitle("Crear tipo de constancia");
        } else {
            btnCreateOrEdit.setText("Actualizar");
            txtNombre.setText(tipo.getNombre());
            setTitle("Actualizar tipo de constancia");
        }
    }

    public void createOrUpdateTipo() throws ServicioException, Exception {
        TipoConstanciaDAO tipoDAO = BeanFactory.local().lookup("TipoConstancia");

        String nombre = txtNombre.getText();

        if (tipo == null) {
            tipo = tipoDAO.create(new TipoConstancia(nombre), selectedFile);

            return;
        }

        tipo.setNombre(nombre);

        tipoDAO.edit(tipo, selectedFile);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtPath = new javax.swing.JTextField();
        btnAttach = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        btnCreateOrEdit = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Nombre");

        jLabel2.setText("Plantilla");

        btnAttach.setText("...");
        btnAttach.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAttachActionPerformed(evt);
            }
        });

        jButton2.setText("Cancelar");

        btnCreateOrEdit.setText("Guardar");
        btnCreateOrEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateOrEditActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNombre)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtPath)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAttach, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 196, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCreateOrEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(12, 12, 12))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnAttach, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                            .addComponent(txtPath))
                        .addContainerGap(95, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 123, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnCreateOrEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12))))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAttachActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAttachActionPerformed
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setDialogTitle("Seleccionar archivo");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("*.pdf", "pdf"));

        int option = fileChooser.showSaveDialog(tiposConstanciaPnl.getParentFrame());

        if (option == JFileChooser.APPROVE_OPTION) {
            this.selectedFile = fileChooser.getSelectedFile();

            txtPath.setText(selectedFile.getAbsolutePath());
        }
    }//GEN-LAST:event_btnAttachActionPerformed

    private void btnCreateOrEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateOrEditActionPerformed
        try {
            createOrUpdateTipo();

            tiposConstanciaPnl.refreshTable();
            
            dispose();
        } catch (ServicioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Ha ocurrido un error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_btnCreateOrEditActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAttach;
    private javax.swing.JButton btnCreateOrEdit;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtPath;
    // End of variables declaration//GEN-END:variables
}
