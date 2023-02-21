package com.fannog.cliente.ui.eventos;

import com.fannog.cliente.utils.BeanFactory;
import com.fannog.servidor.DAO.ModalidadEventoDAO;
import com.fannog.servidor.entities.ModalidadEvento;
import com.fannog.servidor.exceptions.ServicioException;
import javax.swing.JOptionPane;

public class CreateOrEditModalidadEvento extends javax.swing.JDialog {

    private ModalidadEvento modalidadEvento;
    private GestionModalidadEvento gestionModalidadEvento;

    public CreateOrEditModalidadEvento(java.awt.Frame parent, boolean modal, ModalidadEvento modalidadEvento, GestionModalidadEvento gestionModalidadEvento) {
        super(parent, modal);
        this.modalidadEvento = modalidadEvento;
        this.gestionModalidadEvento = gestionModalidadEvento;
        initComponents();

        if (modalidadEvento == null) {
            btnEditOrCreate.setText("Crear");
        } else {
            btnEditOrCreate.setText("Actualizar");
            txtNombre.setText(modalidadEvento.getNombre());
        }

    }

    public void updateModalidadEvento() throws ServicioException, Exception {
        ModalidadEventoDAO modalidadEventoDAO = BeanFactory.local().lookup("ModalidadEvento");

        String newNombre = txtNombre.getText();

        modalidadEvento.setNombre(newNombre);

        modalidadEventoDAO.edit(modalidadEvento);

        gestionModalidadEvento.getModalidadEventoTableModel().fireTableDataChanged();
    }

    public void createModalidadEvento() throws ServicioException, Exception {
        ModalidadEventoDAO modalidadEventoDAO = BeanFactory.local().lookup("ModalidadEvento");

        String nombre = txtNombre.getText();

        ModalidadEvento newModalidadEvento = new ModalidadEvento(nombre);

        ModalidadEvento newMod = modalidadEventoDAO.create(newModalidadEvento);

        gestionModalidadEvento.getModalidadEventoTableModel().getListRows().add(newMod);
        gestionModalidadEvento.getModalidadEventoTableModel().fireTableDataChanged();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        btnCancelar = new javax.swing.JButton();
        btnEditOrCreate = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel2.setText("Nombre:");

        btnCancelar.setText("Cancelar");

        btnEditOrCreate.setText("Editar");
        btnEditOrCreate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEditOrCreateMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 104, Short.MAX_VALUE)
                                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnEditOrCreate, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtNombre))
                        .addGap(12, 12, 12))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEditOrCreate, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEditOrCreateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditOrCreateMouseClicked
        try {
            if (modalidadEvento == null) {
                createModalidadEvento();
                JOptionPane.showMessageDialog(this, "Modalidad de Evento creado con éxito \nRecargue la tabla para visualizar los cambios");
                dispose();
            } else {
                updateModalidadEvento();
                JOptionPane.showMessageDialog(this, "Se ha actualizado la Modalidad de Evento correctamente \nRecargue la tabla para visualizar los cambios");
                dispose();
            }
        } catch (ServicioException ex) {
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_btnEditOrCreateMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEditOrCreate;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
