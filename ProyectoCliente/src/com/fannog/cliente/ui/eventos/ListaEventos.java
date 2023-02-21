package com.fannog.cliente.ui.eventos;

import com.fannog.cliente.utils.BeanFactory;
import com.fannog.cliente.utils.DataTableModel;
import com.fannog.cliente.utils.Globals;
import com.fannog.servidor.DAO.EstadoUsuarioDAO;
import com.fannog.servidor.DAO.EventoDAO;
import com.fannog.servidor.DAO.UsuarioDAO;
import com.fannog.servidor.entities.Analista;
import com.fannog.servidor.entities.EstadoUsuario;
import com.fannog.servidor.entities.Evento;
import com.fannog.servidor.entities.Tutor;
import com.fannog.servidor.entities.Usuario;
import com.fannog.servidor.exceptions.ServicioException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableRowSorter;

public class ListaEventos extends javax.swing.JPanel {

    private TableRowSorter<DataTableModel> sorter;
    private JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
    private DataTableModel<Evento> eventoTableModel = createTablaModelEventos();

    public ListaEventos() {
        initComponents();

        tablaEventos.getSelectionModel().addListSelectionListener(this::handleTableItrsSelection);

        try {
            loadEventos();
            loadDefaultComboBoxFiltro();
        } catch (Exception ex) {
        }
    }

    public void refreshTable() throws Exception {
        loadEventos();

        ((DataTableModel<Evento>) tablaEventos.getModel()).fireTableDataChanged();
    }

    private void loadDefaultComboBoxFiltro() {
        comboFiltro.removeAllItems();

        comboFiltro.addItem("TITULO");
        comboFiltro.addItem("TIPO EVENTO");
        comboFiltro.addItem("ESTADO");
        comboFiltro.addItem("ITR");
        comboFiltro.addItem("MODALIDAD");
        comboFiltro.addItem("LOCALIZACIÓN");
    }

    public DataTableModel<Evento> getEventoTableModel() {
        return eventoTableModel;
    }

    public void loadEventos() throws Exception {
        Usuario usuario = Globals.getLoggedUser();

        if (usuario instanceof Analista) {
            EventoDAO eventoDAO = BeanFactory.local().lookup("Evento");
            List<Evento> eventos = eventoDAO.findAll();

            eventoTableModel.setListRows(eventos);
            tablaEventos.setModel(eventoTableModel);

            return;
        }

        if (usuario instanceof Tutor tutor) {
            List<Evento> eventos = tutor.getEventos();

            eventoTableModel.setListRows(eventos);
            tablaEventos.setModel(eventoTableModel);

            btnCrear.setVisible(false);
            btnModificar.setVisible(false);
            btnBaja.setVisible(false);
        }

        sorter = new TableRowSorter<>(eventoTableModel);

        tablaEventos.setRowSorter(sorter);
    }

    public void loadEventosByDates(LocalDateTime inicio, LocalDateTime fin) throws Exception {
        Usuario usuario = Globals.getLoggedUser();
        EventoDAO eventoDAO = BeanFactory.local().lookup("Evento");
        List<Evento> eventos = eventoDAO.findBetweenDates(inicio, fin);
        if (usuario instanceof Analista) {

            eventoTableModel.setListRows(eventos);
            tablaEventos.setModel(eventoTableModel);
        } else if (usuario instanceof Tutor) {
            Tutor tutor = (Tutor) usuario;
            List<Evento> eventosTutor = tutor.getEventos();

            eventos.retainAll(eventosTutor);

            eventoTableModel.setListRows(eventos);
            tablaEventos.setModel(eventoTableModel);

            btnCrear.setVisible(false);
            btnModificar.setVisible(false);
            btnBaja.setVisible(false);
        }

        sorter = new TableRowSorter<>(eventoTableModel);

        tablaEventos.setRowSorter(sorter);
    }

    public Evento getSelectedEvento() {
        int row = tablaEventos.getSelectedRow();

        Evento evento = (Evento) tablaEventos.getValueAt(row, -1);

        return evento;
    }

    public DataTableModel createTablaModelEventos() {
        String[] COLUMNS = {"TITULO", "TIPO DE EVENTO", "FECHA INICIO", "FECHA FIN", "ESTADO", "ITR", "MODALIDAD", "LOCALIZACIÓN"};

        return new DataTableModel<Evento>(COLUMNS) {
            @Override
            public Object getValueAt(Evento evento, int columnas) {
                return switch (columnas) {
                    case -1 ->
                        evento;
                    case 0 ->
                        evento.getNombre();
                    case 1 ->
                        evento.getTipo().getNombre();
                    case 2 ->
                        evento.getFecHoraInicio();
                    case 3 ->
                        evento.getFecHoraFinal();
                    case 4 ->
                        evento.getEstado().getNombre();
                    case 5 ->
                        evento.getItr().getNombre();
                    case 6 ->
                        evento.getModalidad().getNombre();
                    case 7 ->
                        evento.getLocalizacion();
                    default ->
                        null;
                };
            }
        };
    }

    public void enabledBtnsCRUD(boolean enabled) {
        btnModificar.setEnabled(enabled);
        btnBaja.setEnabled(enabled);
    }

    public void handleTableItrsSelection(ListSelectionEvent e) {
        boolean seleccionValida = tablaEventos.getSelectedRow() != -1;

        if (seleccionValida) {
            enabledBtnsCRUD(true);

            return;
        }

        enabledBtnsCRUD(false);
    }

    public void deleteEvento() throws ServicioException, Exception {

    }

    public void filtrarTabla() {
        try {
            int opcionFiltro = comboFiltro.getSelectedIndex();

            if (opcionFiltro > 1) {
                opcionFiltro = opcionFiltro + 2;
                String textoFiltro = txtFiltro.getText();
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + textoFiltro, opcionFiltro));
            } else {
                String textoFiltro = txtFiltro.getText();
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + textoFiltro, opcionFiltro));
            }

        } catch (Exception e) {
        }
    }

    public void populateComboFiltro() {
        comboFiltro.addItem("NOMBRE");
        comboFiltro.addItem("TIPO DE EVENTO");
        comboFiltro.addItem("MODALIDAD");
        comboFiltro.addItem("ITR");
        comboFiltro.addItem("ESTADO");
    }

    public LocalDateTime convertFecha(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public void bajaUsuario() throws ServicioException, Exception {
        String nombreUsuario = (String) tablaEventos.getValueAt(tablaEventos.getSelectedRow(), 2);
        UsuarioDAO usuarioDAO = BeanFactory.local().lookup("Usuario");
        Usuario usuario = usuarioDAO.findByNombreUsuario(nombreUsuario);

        EstadoUsuarioDAO estadoDAO = BeanFactory.local().lookup("EstadoUsuario");
        EstadoUsuario estado = estadoDAO.findById(3L);

        usuario.setEstado(estado);
        usuarioDAO.edit(usuario);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtFiltro = new javax.swing.JTextField();
        comboFiltro = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaEventos = new javax.swing.JTable();
        chooserInicio = new com.toedter.calendar.JDateChooser();
        chooserFin = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btnFiltrar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        jToolBar1 = new javax.swing.JToolBar();
        btnCrear = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnModificar = new javax.swing.JButton();
        btnBaja = new javax.swing.JButton();

        txtFiltro.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtFiltro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFiltroKeyReleased(evt);
            }
        });

        comboFiltro.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        tablaEventos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tablaEventos);

        chooserInicio.setDateFormatString("dd/MM/yyyy");

        chooserFin.setDateFormatString("dd/MM/yyyy");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setText("Inicio:");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setText("Fin:");

        btnFiltrar.setText("Filtrar");
        btnFiltrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFiltrarActionPerformed(evt);
            }
        });

        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        jToolBar1.setRollover(true);

        btnCrear.setText("Crear");
        btnCrear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCrearActionPerformed(evt);
            }
        });
        jToolBar1.add(btnCrear);
        jToolBar1.add(jSeparator1);

        btnModificar.setText("Modificar");
        btnModificar.setEnabled(false);
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });
        jToolBar1.add(btnModificar);

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
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(txtFiltro, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(comboFiltro, 0, 227, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(chooserInicio, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(chooserFin, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                        .addGap(35, 35, 35)
                        .addComponent(btnLimpiar, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(btnFiltrar, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)))
                .addGap(0, 0, 0))
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(chooserInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(chooserFin, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(9, 9, 9)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(comboFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnFiltrar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtFiltroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltroKeyReleased
        filtrarTabla();
    }//GEN-LAST:event_txtFiltroKeyReleased

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        new AltaOModEvento(parentFrame, true, getSelectedEvento(), this).show();
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnCrearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCrearActionPerformed
        new AltaOModEvento(parentFrame, true, null, this).show();
    }//GEN-LAST:event_btnCrearActionPerformed

    private void btnBajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBajaActionPerformed
        int confirmarcion = JOptionPane.showConfirmDialog(null, "Por favor, confirme que desea eliminar el evento", "Confirmar baja evento", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirmarcion == 0) {
            try {
                EventoDAO eventoDAO = BeanFactory.local().lookup("Evento");
                Evento evento = (Evento) getSelectedEvento();

                eventoDAO.remove(evento);

                eventoTableModel.getListRows().remove(evento);
                eventoTableModel.fireTableDataChanged();
            } catch (Exception ex) {
            }
        }
    }//GEN-LAST:event_btnBajaActionPerformed

    private void btnFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrarActionPerformed
        if (chooserFin.getDate() != null && chooserInicio.getDate() != null) {
            LocalDateTime inicio = convertFecha(chooserInicio.getDate());
            LocalDateTime fin = convertFecha(chooserFin.getDate());

            try {
                loadEventosByDates(inicio, fin);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Debe seleccionar fecha de inicio y final para filtrar", "Filtro Incorrecto", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_btnFiltrarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        try {
            loadEventos();
            chooserFin.setCalendar(null);
            chooserInicio.setCalendar(null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnLimpiarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBaja;
    private javax.swing.JButton btnCrear;
    private javax.swing.JButton btnFiltrar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnModificar;
    private com.toedter.calendar.JDateChooser chooserFin;
    private com.toedter.calendar.JDateChooser chooserInicio;
    private javax.swing.JComboBox<String> comboFiltro;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTable tablaEventos;
    private javax.swing.JTextField txtFiltro;
    // End of variables declaration//GEN-END:variables
}
