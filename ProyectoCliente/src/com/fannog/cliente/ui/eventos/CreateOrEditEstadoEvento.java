package com.fannog.cliente.ui.eventos;

import com.fannog.cliente.utils.BeanFactory;
import com.fannog.servidor.DAO.EstadoEventoDAO;
import com.fannog.servidor.entities.EstadoEvento;
import com.fannog.servidor.exceptions.ServicioException;
import javax.swing.JOptionPane;

public class CreateOrEditEstadoEvento extends javax.swing.JDialog {

    private EstadoEvento estadoEvento;
    private GestionEstadoEventos gestionEstadoEventos1;

    public CreateOrEditEstadoEvento(java.awt.Frame parent, boolean modal, EstadoEvento estadoEvento, GestionEstadoEventos gestionEstadoEventos1) {
        super(parent, modal);
        this.estadoEvento = estadoEvento;
        this.gestionEstadoEventos1 = gestionEstadoEventos1;
        initComponents();

        if (estadoEvento == null) {
            btnEditOrCreate.setText("Crear");
        } else {
            btnEditOrCreate.setText("Actualizar");
            txtNombre.setText(estadoEvento.getNombre());
        }

    }

    public void updateEstadoEvento() throws ServicioException, Exception {
        EstadoEventoDAO estadoEventoDAO = BeanFactory.local().lookup("EstadoEvento");

        String newNombre = txtNombre.getText();

        estadoEvento.setNombre(newNombre);

        gestionEstadoEventos1.getEstadoEventoTableModel().fireTableDataChanged();

        estadoEventoDAO.edit(this.estadoEvento);
    }

    public void createEstadoEvento() throws ServicioException, Exception {
        EstadoEventoDAO estadoEventoDAO = BeanFactory.local().lookup("EstadoEvento");

        String nombre = txtNombre.getText();

        EstadoEvento newEstadoEvento = new EstadoEvento(nombre);

        EstadoEvento newEE =  estadoEventoDAO.create(newEstadoEvento);

        gestionEstadoEventos1.getEstadoEventoTableModel().getListRows().add(newEE);
        gestionEstadoEventos1.getEstadoEventoTableModel().fireTableDataChanged();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        btnCancelar = new javax.swing.JButton();
        btnEditOrCreate = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 123, Short.MAX_VALUE)
                        .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnCancelar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEditOrCreate)))
                .addGap(12, 12, 12))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(95, 95, 95)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEditOrCreate)
                    .addComponent(btnCancelar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEditOrCreateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditOrCreateMouseClicked
        try {
            if (estadoEvento == null) {
                createEstadoEvento();
                JOptionPane.showMessageDialog(this, "Estado de Evento creado con éxito \nRecargue la tabla para visualizar los cambios");
                dispose();
            } else {
                updateEstadoEvento();
                JOptionPane.showMessageDialog(this, "Se ha actualizado el Estado de Evento correctamente \nRecargue la tabla para visualizar los cambios");
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
