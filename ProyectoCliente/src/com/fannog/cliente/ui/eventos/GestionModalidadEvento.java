package com.fannog.cliente.ui.eventos;

import com.fannog.cliente.utils.BeanFactory;
import com.fannog.cliente.utils.DataTableModel;
import com.fannog.servidor.DAO.ModalidadEventoDAO;
import com.fannog.servidor.entities.ModalidadEvento;
import com.fannog.servidor.exceptions.ServicioException;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;

public class GestionModalidadEvento extends javax.swing.JPanel {

    private JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
    private DataTableModel<ModalidadEvento> modalidadEventoTableModel = createModalidadEventoTableModel();

    public GestionModalidadEvento() {
        initComponents();

        tablaModalidadEvento.getSelectionModel().addListSelectionListener(this::handleTableIModalidadSelection);

        try {
            loadModalidadEvento();
        } catch (Exception ex) {
        }
    }

    public DataTableModel<ModalidadEvento> getModalidadEventoTableModel() {
        return modalidadEventoTableModel;
    }

    private void loadModalidadEvento() throws Exception {
        ModalidadEventoDAO modalidadEventoDAO = BeanFactory.local().lookup("ModalidadEvento");
        List<ModalidadEvento> modalidadesEventos = modalidadEventoDAO.findAll();

        modalidadEventoTableModel.setListRows(modalidadesEventos);
        tablaModalidadEvento.setModel(modalidadEventoTableModel);
    }

    public ModalidadEvento getSelectedModalidadEvento() {
        int row = tablaModalidadEvento.getSelectedRow();

        ModalidadEvento modalidadEvento = (ModalidadEvento) tablaModalidadEvento.getValueAt(row, -1);

        return modalidadEvento;
    }

    public DataTableModel createModalidadEventoTableModel() {

        String[] COLUMNS = {"NOMBRE"};

        return new DataTableModel<ModalidadEvento>(COLUMNS) {
            @Override
            public Object getValueAt(ModalidadEvento modalidadEvento, int columnas) {
                return switch (columnas) {
                    case -1 ->
                        modalidadEvento;
                    case 0 ->
                        modalidadEvento.getNombre();
                    default ->
                        null;
                };
            }
        };
    }

    private void enableBtnsCRUD(boolean enabled) {
        btnBaja.setEnabled(enabled);
        btnEditar.setEnabled(enabled);
    }

    public void handleTableIModalidadSelection(ListSelectionEvent e) {
        boolean seleccionValida = tablaModalidadEvento.getSelectedRow() != -1;

        if (seleccionValida) {
            enableBtnsCRUD(true);

            return;
        }

        enableBtnsCRUD(false);
    }

    public void deleteModalidadEvento() throws ServicioException, Exception {
        ModalidadEventoDAO modalidadEventoDAO = BeanFactory.local().lookup("ModalidadEvento");
        ModalidadEvento modalidadEvento = getSelectedModalidadEvento();

        modalidadEventoDAO.remove(modalidadEvento);

        modalidadEventoTableModel.getListRows().remove(modalidadEvento);
        modalidadEventoTableModel.fireTableDataChanged();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tablaModalidadEvento = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        btnCrear = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnBaja = new javax.swing.JButton();

        tablaModalidadEvento.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tablaModalidadEvento.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaModalidadEventoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaModalidadEvento);

        jToolBar1.setRollover(true);

        btnCrear.setText("Crear");
        btnCrear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCrearActionPerformed(evt);
            }
        });
        jToolBar1.add(btnCrear);

        btnEditar.setText("Editar");
        btnEditar.setEnabled(false);
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });
        jToolBar1.add(btnEditar);

        btnBaja.setText("Baja");
        btnBaja.setEnabled(false);
        btnBaja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBajaActionPerformed(evt);
            }
        });
        jToolBar1.add(btnBaja);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 596, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 517, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tablaModalidadEventoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaModalidadEventoMouseClicked
        // TODO add your handling code here:
        btnEditar.setEnabled(true);
    }//GEN-LAST:event_tablaModalidadEventoMouseClicked

    private void btnBajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBajaActionPerformed
        // TODO add your handling code here:
        try {
            deleteModalidadEvento();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnBajaActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        // TODO add your handling code here:
        new CreateOrEditModalidadEvento(parentFrame, true, getSelectedModalidadEvento(), this).show();
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnCrearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCrearActionPerformed
        // TODO add your handling code here:
        new CreateOrEditModalidadEvento(parentFrame, true, null, this).show();
    }//GEN-LAST:event_btnCrearActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBaja;
    private javax.swing.JButton btnCrear;
    private javax.swing.JButton btnEditar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTable tablaModalidadEvento;
    // End of variables declaration//GEN-END:variables
}
