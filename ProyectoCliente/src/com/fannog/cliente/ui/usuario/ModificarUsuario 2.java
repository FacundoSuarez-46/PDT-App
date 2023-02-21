package com.fannog.cliente.ui.usuario;

import com.fannog.cliente.ui.analista.ListaUsuarios;
import com.fannog.cliente.utils.BeanFactory;
import com.fannog.cliente.utils.Globals;
import com.fannog.servidor.DAO.UsuarioDAO;
import com.fannog.servidor.entities.Usuario;
import com.fannog.servidor.exceptions.ServicioException;
import javax.swing.JOptionPane;

public class ModificarUsuario extends javax.swing.JDialog {

    private Usuario usuario;
    private DatosPersonales pnlDatosPersonales;
    private ListaUsuarios pnlListaUsuarios;

    public ModificarUsuario(java.awt.Frame parent, boolean modal, Usuario usuario, DatosPersonales pnlDatosPersonales, ListaUsuarios pnlListaUsuarios) {
        super(parent, modal);
        this.usuario = usuario;
        this.pnlDatosPersonales = pnlDatosPersonales;
        this.pnlListaUsuarios = pnlListaUsuarios;

        initComponents();

        try {
            loadUsuario();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadUsuario() throws Exception {
        form.setUsuario(usuario);

        form.loadData();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        form = new com.fannog.cliente.ui.usuario.UsuarioForm();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(form, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(12, 12, 12))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(form, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        try {
            form.saveData();

            UsuarioDAO usuarioDAO = BeanFactory.local().lookup("Usuario");
            Usuario updateUsuario = form.getUsuario();

            usuarioDAO.edit(updateUsuario);

            if (updateUsuario.getId() == Globals.getLoggedUser().getId()) {
                Globals.setLoggedUser(updateUsuario);

                pnlDatosPersonales.loadFields();
            } else {
                pnlListaUsuarios.refreshTable();
            }

            dispose();
        } catch (ServicioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Ha ocurrido un error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private com.fannog.cliente.ui.usuario.UsuarioForm form;
    // End of variables declaration//GEN-END:variables
}
