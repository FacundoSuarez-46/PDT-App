package com.fannog.cliente.ui.estudiante;

import com.fannog.cliente.utils.BeanFactory;
import com.fannog.cliente.utils.DataTableModel;
import com.fannog.cliente.utils.Globals;
import com.fannog.servidor.DAO.AccionSolicitudDAO;
import com.fannog.servidor.DAO.EstadoSolicitudDAO;
import com.fannog.servidor.DAO.SolicitudDAO;
import com.fannog.servidor.entities.AccionSolicitud;
import com.fannog.servidor.entities.EstadoSolicitud;
import com.fannog.servidor.entities.Solicitud;
import java.awt.Component;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.TableRowSorter;

public class ListadoSolicitudes extends javax.swing.JPanel {

    private TableRowSorter<DataTableModel> sorter;
    private DataTableModel<Solicitud> solicitudTableModel = createSolicitudModel();
    private JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

    public DataTableModel<Solicitud> getSolicitudTableModel() {
        return solicitudTableModel;
    }

    public ListadoSolicitudes() {
        initComponents();
        myComponentsInit();

        try {
            populatecomboEstadoSolicitud();
        } catch (Exception ex) {
        }
    }

    private void myComponentsInit() {
        activarBotonesUD(false);

        jTableSolicitudes.getSelectionModel().addListSelectionListener(this::handleTableSolicitudesSelectionEvent);
        ((DefaultTableCellRenderer) jTableSolicitudes.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.LEFT);

        try {
            loadSolicitudes();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void refreshTable() throws Exception {
        loadSolicitudes();
        ((DataTableModel) jTableSolicitudes.getModel()).fireTableDataChanged();
    }

    public void activarBotonesUD(boolean activar) {
        btnModificar.setEnabled(activar);
        btnBorrar.setEnabled(activar);
    }

    private void populatecomboEstadoSolicitud() throws Exception {
        EstadoSolicitudDAO estadoSolicitudDAO = BeanFactory.local().lookup("EstadoSolicitud");

        List<EstadoSolicitud> estadoSolicitudes = estadoSolicitudDAO.findAll();

        comboEstadoSolicitud.setModel(new DefaultComboBoxModel(estadoSolicitudes.toArray()));
        comboEstadoSolicitud.addItem("TODOS");
        comboEstadoSolicitud.setSelectedItem("TODOS");
    }

    private void filterTable(String estado) {
        try {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + estado, 2));
        } catch (Exception e) {
        }
    }

    private Long getIdLoggedUser() {
        Long id = Globals.getLoggedUser().getId();
        return id;
    }

    public void loadSolicitudes() throws Exception {

        SolicitudDAO solicitudDAO = BeanFactory.local().lookup("Solicitud");

        List<Solicitud> solicitudes = solicitudDAO.findByEstudiante(getIdLoggedUser());

        solicitudTableModel.setListRows(solicitudes);
        jTableSolicitudes.setModel(solicitudTableModel);

        jTableSolicitudes.setAutoCreateRowSorter(true);
        sorter = new TableRowSorter<>(getSolicitudTableModel());
        jTableSolicitudes.setRowSorter(sorter);
    }

    public DataTableModel createSolicitudModel() {

        String[] COLUMNS = {"Detalle", "Fecha", "Estado", "Tipo"};

        return new DataTableModel<Solicitud>(COLUMNS) {
            @Override
            public Object getValueAt(Solicitud solicitud, int columnas) {
                return switch (columnas) {
                    case -1 ->
                        solicitud;
                    case 0 ->
                        solicitud.getDetalle();
                    case 1 ->
                        solicitud.getFecha().format(Globals.dateTimeFormatter);
                    case 2 ->
                        solicitud.getEstado().getNombre();
                    case 3 ->
                        solicitud.getTipo().getNombre();
                    default ->
                        null;
                };
            }
        };
    }

    public Solicitud getSelectedSolicitud() {
        int modelRow = jTableSolicitudes.convertRowIndexToModel​(jTableSolicitudes.getSelectedRow());
        DataTableModel model = (DataTableModel) jTableSolicitudes.getModel();

        Solicitud s = (Solicitud) model.getValueAt(modelRow, -1);

        return s;
    }

    public void deleteSolicitud() {

        try {
            AccionSolicitudDAO accionesSolicitudDAO = BeanFactory.local().lookup("AccionSolicitud");
            List<AccionSolicitud> acciones = accionesSolicitudDAO.findAllBySolicitud(getSelectedSolicitud().getId());

            if (JOptionPane.showConfirmDialog(this,
                    "¿Estas seguro que quieres borrar esta solicitud?", "Borrar Solicitud", JOptionPane.YES_NO_OPTION, 3) == JOptionPane.YES_OPTION) {

                if (acciones.isEmpty()) {
                    System.out.println("If acciones is empty");
                    System.out.println("acciones: " + acciones);
                    try {
                        SolicitudDAO solicitudDAO = BeanFactory.local().lookup("Solicitud");

                        solicitudDAO.remove(getSelectedSolicitud());
                        getSolicitudTableModel().getListRows().remove(getSelectedSolicitud());
                        getSolicitudTableModel().fireTableDataChanged();

                        JOptionPane.showMessageDialog(parentFrame, "La solicitud fue eliminada con exito!");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(parentFrame, "No se pudo borrar la solicitud seleccionada!");
                    }
                } else {
                    JOptionPane.showMessageDialog(parentFrame, "No se pudo borrar la solicitud, tiene acciones vinculadas!");
                }
            }
        } catch (Exception ex) {
        }

    }

    public void handleTableSolicitudesSelectionEvent(ListSelectionEvent e) {
        boolean seleccionValida = jTableSolicitudes.getSelectedRow() != -1;

        activarBotonesUD(seleccionValida);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        btnNuevo = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnModificar = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        btnBorrar = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        comboEstadoSolicitud = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableSolicitudes = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();

        jToolBar1.setRollover(true);

        btnNuevo.setText("Nuevo");
        btnNuevo.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnNuevo.setFocusable(false);
        btnNuevo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNuevo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNuevo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNuevoMouseClicked(evt);
            }
        });
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });
        jToolBar1.add(btnNuevo);
        jToolBar1.add(jSeparator1);

        btnModificar.setText("Modificar");
        btnModificar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnModificar.setFocusable(false);
        btnModificar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnModificar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });
        jToolBar1.add(btnModificar);
        jToolBar1.add(jSeparator3);

        btnBorrar.setText("Borrar");
        btnBorrar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnBorrar.setFocusable(false);
        btnBorrar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBorrar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnBorrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBorrarMouseClicked(evt);
            }
        });
        btnBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarActionPerformed(evt);
            }
        });
        jToolBar1.add(btnBorrar);
        jToolBar1.add(jSeparator2);

        comboEstadoSolicitud.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof EstadoSolicitud) {
                    EstadoSolicitud estadoSolicitud = (EstadoSolicitud) value;
                    setText(estadoSolicitud.getNombre());
                }
                return this;
            }
        });
        comboEstadoSolicitud.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboEstadoSolicitudActionPerformed(evt);
            }
        });

        jTableSolicitudes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTableSolicitudes.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTableSolicitudes.getTableHeader().setReorderingAllowed(false);
        jTableSolicitudes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableSolicitudesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableSolicitudes);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Estado");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(141, 141, 141)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(comboEstadoSolicitud, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1015, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(comboEstadoSolicitud, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnNuevoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNuevoMouseClicked

    }//GEN-LAST:event_btnNuevoMouseClicked

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed

        try {
            new NuevaSolicitud(parentFrame, false, this).setVisible(true);
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        try {
            new EditarSolicitud(parentFrame, false, this).setVisible(true);
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnBorrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBorrarMouseClicked

    }//GEN-LAST:event_btnBorrarMouseClicked

    private void btnBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarActionPerformed
        deleteSolicitud();
    }//GEN-LAST:event_btnBorrarActionPerformed

    private void comboEstadoSolicitudActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboEstadoSolicitudActionPerformed
        if (!(comboEstadoSolicitud.getSelectedItem() instanceof EstadoSolicitud)) {
            sorter.setRowFilter(null);

            return;
        }

        EstadoSolicitud estado = (EstadoSolicitud) comboEstadoSolicitud.getSelectedItem();
        String strEstado = estado.getNombre();

        filterTable(strEstado);
    }//GEN-LAST:event_comboEstadoSolicitudActionPerformed

    private void jTableSolicitudesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableSolicitudesMouseClicked

        if (evt.getClickCount() > 1) {
            try {
                new FichaSolicitud(parentFrame, false, this).setVisible(true);
            } catch (Exception ex) {
            }
        }

    }//GEN-LAST:event_jTableSolicitudesMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBorrar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JComboBox<String> comboEstadoSolicitud;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JTable jTableSolicitudes;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
