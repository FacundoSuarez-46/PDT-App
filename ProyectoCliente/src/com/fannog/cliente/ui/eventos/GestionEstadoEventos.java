package com.fannog.cliente.ui.eventos;

import com.fannog.cliente.utils.BeanFactory;
import com.fannog.cliente.utils.DataTableModel;
import com.fannog.servidor.DAO.EstadoEventoDAO;
import com.fannog.servidor.entities.EstadoEvento;
import com.fannog.servidor.exceptions.ServicioException;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;

public class GestionEstadoEventos extends javax.swing.JPanel {

    private JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
    private DataTableModel<EstadoEvento> estadoEventosTableModel = createEstadoEventosTableModel();

    public GestionEstadoEventos() {
        initComponents();

        tablaEstadoEventos.getSelectionModel().addListSelectionListener(this::handleTableIEESelection);

        try {
            loadEstadoEvento();
        } catch (Exception ex) {
        }
    }

    public DataTableModel<EstadoEvento> getEstadoEventoTableModel() {
        return estadoEventosTableModel;
    }

    private void loadEstadoEvento() throws Exception {
        EstadoEventoDAO estadoEventoDAO = BeanFactory.local().lookup("EstadoEvento");
        List<EstadoEvento> estadoEventos = estadoEventoDAO.findAll();

        estadoEventosTableModel.setListRows(estadoEventos);
        tablaEstadoEventos.setModel(estadoEventosTableModel);
    }

    public EstadoEvento getSelectedEstadoEvento() {
        int row = tablaEstadoEventos.getSelectedRow();

        EstadoEvento estadoEvento = (EstadoEvento) tablaEstadoEventos.getValueAt(row, -1);

        return estadoEvento;
    }

    public DataTableModel createEstadoEventosTableModel() {

        String[] COLUMNS = {"NOMBRE"};

        return new DataTableModel<EstadoEvento>(COLUMNS) {
            @Override
            public Object getValueAt(EstadoEvento estadoEvento, int columnas) {
                return switch (columnas) {
                    case -1 ->
                        estadoEvento;
                    case 0 ->
                        estadoEvento.getNombre();
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

    public void handleTableIEESelection(ListSelectionEvent e) {
        boolean seleccionValida = tablaEstadoEventos.getSelectedRow() != -1;

        if (seleccionValida) {
            enableBtnsCRUD(true);

            return;
        }

        enableBtnsCRUD(false);
    }

    public void deleteEstadoEvento() throws ServicioException, Exception {
        EstadoEventoDAO estadoEventoDAO = BeanFactory.local().lookup("EstadoEvento");
        EstadoEvento estadoEvento = getSelectedEstadoEvento();

        estadoEventosTableModel.getListRows().remove(estadoEvento);
        estadoEventosTableModel.fireTableDataChanged();

        estadoEventoDAO.remove(estadoEvento);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tablaEstadoEventos = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        btnCrear = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnBaja = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();

        tablaEstadoEventos.setModel(new javax.swing.table.DefaultTableModel(
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
        tablaEstadoEventos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaEstadoEventosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaEstadoEventos);

        jToolBar1.setRollover(true);

        btnCrear.setText("Crear");
        btnCrear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCrearActionPerformed(evt);
            }
        });
        jToolBar1.add(btnCrear);
        jToolBar1.add(jSeparator1);

        btnBaja.setText("Baja");
        btnBaja.setEnabled(false);
        btnBaja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBajaActionPerformed(evt);
            }
        });
        jToolBar1.add(btnBaja);

        btnEditar.setText("Editar");
        btnEditar.setEnabled(false);
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });
        jToolBar1.add(btnEditar);

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

    private void tablaEstadoEventosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaEstadoEventosMouseClicked
        btnEditar.setEnabled(true);
    }//GEN-LAST:event_tablaEstadoEventosMouseClicked

    private void btnBajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBajaActionPerformed
        try {
            deleteEstadoEvento();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnBajaActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        new CreateOrEditEstadoEvento(parentFrame, true, getSelectedEstadoEvento(), this).show();
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnCrearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCrearActionPerformed
        new CreateOrEditEstadoEvento(parentFrame, true, null, this).show();
    }//GEN-LAST:event_btnCrearActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBaja;
    private javax.swing.JButton btnCrear;
    private javax.swing.JButton btnEditar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTable tablaEstadoEventos;
    // End of variables declaration//GEN-END:variables
}
