package com.fannog.cliente.ui.analista;

import com.docmosis.SystemManager;
import com.docmosis.document.DocumentProcessor;
import com.docmosis.template.population.DataProviderBuilder;
import com.fannog.cliente.utils.BeanFactory;
import com.fannog.cliente.utils.DataTableModel;
import com.fannog.cliente.utils.Globals;
import com.fannog.servidor.DAO.AccionSolicitudDAO;
import com.fannog.servidor.DAO.AdjuntoSolicitudDAO;
import com.fannog.servidor.DAO.EstadoSolicitudDAO;
import com.fannog.servidor.DAO.SolicitudDAO;
import com.fannog.servidor.entities.AccionSolicitud;
import com.fannog.servidor.entities.AdjuntoSolicitud;
import com.fannog.servidor.entities.EstadoSolicitud;
import com.fannog.servidor.entities.Solicitud;
import java.awt.Component;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;

public class Solicitudes extends javax.swing.JPanel {

    private TableRowSorter<DataTableModel> sorter;
    private DataTableModel<Solicitud> solicitudTableModel = createSolicitudModel();
    private JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

    public JFrame getParentFrame() {
        return parentFrame;
    }

    public Solicitudes() {
        initComponents();

        enableTabs(false);
        enableOperationBtns(false, false);
        btnDescargar.setEnabled(false);

        tableSolicitudes.getSelectionModel().addListSelectionListener(this::handleTableSolicitudesSelectionEvent);
        tableSolicitudes.setAutoCreateRowSorter(true);
        listAdjuntos.getSelectionModel().addListSelectionListener(this::handleListAdjuntosSelection);

        ((DefaultTableCellRenderer) tableSolicitudes.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.LEFT);

        try {
            loadSolicitudes();
            loadEstados();
        } catch (Exception ex) {
        }
    }

    public void refreshTable() throws Exception {
        loadSolicitudes();

        ((DataTableModel<Solicitud>) tableSolicitudes.getModel()).fireTableDataChanged();
    }

    public void loadSolicitudes() throws Exception {
        SolicitudDAO solicitudDAO = BeanFactory.local().lookup("Solicitud");

        List<Solicitud> solicitudes = solicitudDAO.findAllWithRelations();
        solicitudTableModel.setListRows(solicitudes);
        tableSolicitudes.setModel(solicitudTableModel);

        sorter = new TableRowSorter<>(solicitudTableModel);
        tableSolicitudes.setRowSorter(sorter);
    }

    public void loadAdjuntos(Long idSolicitud) throws Exception {
        AdjuntoSolicitudDAO adjuntoSolicitudDAO = BeanFactory.local().lookup("AdjuntoSolicitud");

        List<AdjuntoSolicitud> adjuntosSolicitud = adjuntoSolicitudDAO.findAllBySolicitud(idSolicitud);

        DefaultListModel listModel = new DefaultListModel<AdjuntoSolicitud>();

        listModel.addAll(adjuntosSolicitud);

        listAdjuntos.setModel(listModel);
    }

    public void loadAcciones(Long idSolicitud) throws Exception {
        AccionSolicitudDAO accionSolicitudDAO = BeanFactory.local().lookup("AccionSolicitud");

        List<AccionSolicitud> accionesSolicitud = accionSolicitudDAO.findAllBySolicitud(idSolicitud);

        DefaultListModel listModel = new DefaultListModel<AccionSolicitud>();

        listModel.addAll(accionesSolicitud);

        listAcciones.setModel(listModel);
    }

    public void loadEstados() throws Exception {
        EstadoSolicitudDAO estadoDAO = BeanFactory.local().lookup("EstadoSolicitud");

        List<EstadoSolicitud> estados = estadoDAO.findAll();

        DefaultComboBoxModel comboModel = new DefaultComboBoxModel(estados.toArray());

        comboEstados.setModel(comboModel);
        comboModel.addElement("TODOS");
        comboModel.setSelectedItem("TODOS");
    }

    private void enableOperationBtns(boolean enabled, boolean emitida) {
        btnCargarPlantilla.setEnabled(enabled);
        btnRegistrarAccion.setEnabled(enabled);
        btnEmitir.setEnabled(enabled && !emitida);
    }

    private void enableTabs(boolean enabled) {
        jTabbedPane2.setEnabledAt(0, enabled);
        jTabbedPane2.setEnabledAt(1, enabled);
    }

    public DataTableModel createSolicitudModel() {

        String[] COLUMNS = {"Estudiante", "Tipo", "Estado"};

        return new DataTableModel<Solicitud>(COLUMNS) {
            @Override
            public Object getValueAt(Solicitud solicitud, int columnas) {
                return switch (columnas) {
                    case -1 ->
                        solicitud;
                    case 0 ->
                        solicitud.getEstudiante().getNombreUsuario();
                    case 1 ->
                        solicitud.getTipo().getNombre();
                    case 2 ->
                        solicitud.getEstado().getNombre();
                    default ->
                        null;
                };
            }
        };
    }

    public Solicitud getSelectedSolicitud() {
        int modelRow = tableSolicitudes.convertRowIndexToModel​(tableSolicitudes.getSelectedRow());
        DataTableModel model = (DataTableModel) tableSolicitudes.getModel();

        Solicitud s = (Solicitud) model.getValueAt(modelRow, -1);

        return s;
    }

    public AdjuntoSolicitud getSelectedAdjunto() {
        return listAdjuntos.getSelectedValue();
    }

    public void handleTableSolicitudesSelectionEvent(ListSelectionEvent e) {
        boolean seleccionValida = tableSolicitudes.getSelectedRow() != -1;
        boolean emitida = false;

        if (seleccionValida) {
            Solicitud solicitud = getSelectedSolicitud();

            emitida = solicitud.getEstado().getId() == 3L;

            try {
                loadAcciones(solicitud.getId());
                loadAdjuntos(solicitud.getId());
            } catch (Exception ex) {
            }
        }

        enableTabs(seleccionValida);
        enableOperationBtns(seleccionValida, emitida);
    }

    public void handleListAdjuntosSelection(ListSelectionEvent e) {
        boolean seleccionValida = !listAdjuntos.isSelectionEmpty();

        btnDescargar.setEnabled(seleccionValida);
    }

    private void filterTable() {
        RowFilter rw = null;

        List<RowFilter<Object, Object>> filters = new ArrayList<>(2);

        String strEstado = null;

        try {
            EstadoSolicitud estado = (EstadoSolicitud) comboEstados.getSelectedItem();
            strEstado = estado.getNombre();
        } catch (Exception e) {
        }

        int index = comboColumnas.getSelectedIndex();
        String input = txtFiltro.getText().trim();

        try {
            if (strEstado != null) {
                strEstado = Pattern.quote(strEstado);
                String regex = String.format("^%s$", strEstado);

                filters.add(RowFilter.regexFilter(regex, 2));
            }

            if (!input.isEmpty()) {
                filters.add(RowFilter.regexFilter("(?i)" + input, index));
            }

            rw = RowFilter.andFilter(filters);
        } catch (Exception e) {
            return;
        }

        sorter.setRowFilter(rw);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        btnRefresh = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnCargarPlantilla = new javax.swing.JButton();
        btnRegistrarAccion = new javax.swing.JButton();
        btnEmitir = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableSolicitudes = new javax.swing.JTable();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listAdjuntos = new javax.swing.JList<>();
        jToolBar2 = new javax.swing.JToolBar();
        btnDescargar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        listAcciones = new javax.swing.JList<>();
        txtFiltro = new javax.swing.JTextField();
        comboEstados = new javax.swing.JComboBox<>();
        comboColumnas = new javax.swing.JComboBox<>();

        jToolBar1.setRollover(true);

        btnRefresh.setText("Refrescar");
        btnRefresh.setFocusable(false);
        btnRefresh.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRefresh.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });
        jToolBar1.add(btnRefresh);
        jToolBar1.add(jSeparator1);

        btnCargarPlantilla.setFont(new java.awt.Font("Source Sans Pro", 0, 18)); // NOI18N
        btnCargarPlantilla.setText("Cargar en plantilla");
        btnCargarPlantilla.setFocusable(false);
        btnCargarPlantilla.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCargarPlantilla.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCargarPlantilla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCargarPlantillaActionPerformed(evt);
            }
        });
        jToolBar1.add(btnCargarPlantilla);

        btnRegistrarAccion.setFont(new java.awt.Font("Source Sans Pro", 0, 18)); // NOI18N
        btnRegistrarAccion.setText("Registrar acción");
        btnRegistrarAccion.setFocusable(false);
        btnRegistrarAccion.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRegistrarAccion.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRegistrarAccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarAccionActionPerformed(evt);
            }
        });
        jToolBar1.add(btnRegistrarAccion);

        btnEmitir.setFont(new java.awt.Font("Source Sans Pro", 0, 18)); // NOI18N
        btnEmitir.setText("Emitir");
        btnEmitir.setFocusable(false);
        btnEmitir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnEmitir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEmitir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmitirActionPerformed(evt);
            }
        });
        jToolBar1.add(btnEmitir);

        tableSolicitudes.setFont(new java.awt.Font("Source Sans Pro", 0, 18)); // NOI18N
        tableSolicitudes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tableSolicitudes.setRowHeight(30);
        tableSolicitudes.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableSolicitudes.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tableSolicitudes);

        jTabbedPane2.setFont(new java.awt.Font("Source Sans Pro", 0, 18)); // NOI18N

        listAdjuntos.setFont(new java.awt.Font("Source Sans Pro", 0, 18)); // NOI18N
        jScrollPane1.setViewportView(listAdjuntos);

        jToolBar2.setRollover(true);

        btnDescargar.setFont(new java.awt.Font("Source Sans Pro", 0, 18)); // NOI18N
        btnDescargar.setText("Descargar");
        btnDescargar.setFocusable(false);
        btnDescargar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDescargar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDescargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDescargarActionPerformed(evt);
            }
        });
        jToolBar2.add(btnDescargar);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Archivos adj.", jPanel2);

        jPanel3.setFont(new java.awt.Font("Source Sans Pro", 0, 18)); // NOI18N

        listAcciones.setFont(new java.awt.Font("Source Sans Pro", 0, 18)); // NOI18N
        listAcciones.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        listAcciones.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value instanceof AccionSolicitud) {
                    AccionSolicitud a = (AccionSolicitud) value;

                    String parsedFechaHora = a.getFecHora().format(Globals.dateTimeFormatter);
                    String text = "<html>" + a.getDetalle() + "<br/>(" + parsedFechaHora + ")" + " </html>";

                    setText(text);
                }

                return this;
            }
        }
    );
    listAcciones.setLayoutOrientation(javax.swing.JList.HORIZONTAL_WRAP);
    listAcciones.setVisibleRowCount(-1);
    jScrollPane3.setViewportView(listAcciones);

    javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
    jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout.setHorizontalGroup(
        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel3Layout.createSequentialGroup()
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
            .addContainerGap())
    );
    jPanel3Layout.setVerticalGroup(
        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel3Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 606, Short.MAX_VALUE)
            .addContainerGap())
    );

    jTabbedPane2.addTab("Acciones", jPanel3);

    txtFiltro.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyTyped(java.awt.event.KeyEvent evt) {
            txtFiltroKeyTyped(evt);
        }
    });

    comboEstados.setRenderer(new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof EstadoSolicitud) {
                EstadoSolicitud estado = (EstadoSolicitud) value;
                setText(estado.getNombre());
            }
            return this;
        }
    });
    comboEstados.addItemListener(new java.awt.event.ItemListener() {
        public void itemStateChanged(java.awt.event.ItemEvent evt) {
            comboEstadosItemStateChanged(evt);
        }
    });

    comboColumnas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ESTUDIANTE", "TIPO" }));

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addGap(0, 0, 0)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(txtFiltro)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(comboColumnas, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(8, 8, 8)
                            .addComponent(comboEstados, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jTabbedPane2)
                    .addGap(0, 0, 0))
                .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jTabbedPane2)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtFiltro, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                        .addComponent(comboEstados, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                        .addComponent(comboColumnas))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane2)))
            .addGap(0, 0, 0))
    );
    }// </editor-fold>//GEN-END:initComponents

    private void btnDescargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDescargarActionPerformed
        AdjuntoSolicitud adjunto = getSelectedAdjunto();

        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("*.pdf", "pdf"));
        fileChooser.setSelectedFile(new File(adjunto.getNombArchivo()));
        fileChooser.setDialogTitle("Descargar archivo");

        int option = fileChooser.showSaveDialog(parentFrame);

        if (option == JFileChooser.APPROVE_OPTION) {
            String strPath = fileChooser.getSelectedFile().getAbsolutePath();

            Path path = new File(strPath).toPath();

            byte[] fileContent = adjunto.getArchivo();

            try {
                Files.write(path, fileContent);
            } catch (IOException ex) {
            }
        }
    }//GEN-LAST:event_btnDescargarActionPerformed

    private void btnCargarPlantillaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargarPlantillaActionPerformed
        Solicitud solicitud = getSelectedSolicitud();

        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("*.pdf", "pdf"));
        fileChooser.setDialogTitle("Cargar los datos en plantilla");

        int option = fileChooser.showSaveDialog(parentFrame);

        if (option == JFileChooser.APPROVE_OPTION) {
            byte[] plantillaContent = solicitud.getTipo().getPlantilla();

            FileOutputStream fos = null;
            File templateFile = null;

            SystemManager.initialise();

            DataProviderBuilder dpb = new DataProviderBuilder();

            dpb.addJavaObject(solicitud.getEstudiante(), "estudiante");
            dpb.addJavaObject(solicitud.getEvento(), "evento");

            try {
                templateFile = File.createTempFile("plantilla", ".doc", null);
                fos = new FileOutputStream(templateFile);
                fos.write(plantillaContent);

                if (templateFile.canRead()) {
                    DocumentProcessor.renderDoc(templateFile, fileChooser.getSelectedFile(), dpb.getDataProvider());

                    Desktop.getDesktop().open(fileChooser.getSelectedFile());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException ex) {
                }

                templateFile.delete();
                SystemManager.release();
            }
        }
    }//GEN-LAST:event_btnCargarPlantillaActionPerformed

    private void btnRegistrarAccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarAccionActionPerformed
        new NewAccion(parentFrame, true, getSelectedSolicitud(), this).show();
     }//GEN-LAST:event_btnRegistrarAccionActionPerformed

    private void btnEmitirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmitirActionPerformed
        new Emitir(parentFrame, true, getSelectedSolicitud(), this).show();
    }//GEN-LAST:event_btnEmitirActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        try {
            refreshTable();
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void comboEstadosItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboEstadosItemStateChanged
        filterTable();
    }//GEN-LAST:event_comboEstadosItemStateChanged

    private void txtFiltroKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltroKeyTyped
        filterTable();
    }//GEN-LAST:event_txtFiltroKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCargarPlantilla;
    private javax.swing.JButton btnDescargar;
    private javax.swing.JButton btnEmitir;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnRegistrarAccion;
    private javax.swing.JComboBox<String> comboColumnas;
    private javax.swing.JComboBox<String> comboEstados;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JList<String> listAcciones;
    private javax.swing.JList<AdjuntoSolicitud> listAdjuntos;
    private javax.swing.JTable tableSolicitudes;
    private javax.swing.JTextField txtFiltro;
    // End of variables declaration//GEN-END:variables
}
