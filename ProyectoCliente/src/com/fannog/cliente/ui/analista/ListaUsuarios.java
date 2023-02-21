package com.fannog.cliente.ui.analista;

import com.fannog.cliente.ui.usuario.ModificarUsuario;
import com.fannog.cliente.utils.BeanFactory;
import com.fannog.cliente.utils.DataTableModel;
import com.fannog.cliente.utils.Globals;
import com.fannog.servidor.DAO.AnalistaDAO;
import com.fannog.servidor.DAO.EstadoUsuarioDAO;
import com.fannog.servidor.DAO.EstudianteDAO;
import com.fannog.servidor.DAO.TutorDAO;
import com.fannog.servidor.DAO.UsuarioDAO;
import com.fannog.servidor.entities.Analista;
import com.fannog.servidor.entities.EstadoUsuario;
import com.fannog.servidor.entities.Estudiante;
import com.fannog.servidor.entities.Tutor;
import com.fannog.servidor.entities.Usuario;
import com.fannog.servidor.exceptions.ServicioException;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;

public class ListaUsuarios extends javax.swing.JPanel {

    private TableRowSorter<DataTableModel> sorter;

    private DataTableModel<Estudiante> estudiantesTableModel = createEstudiantesTableModel();
    private DataTableModel<Analista> analistasTableModel = createAnalistasTableModel();
    private DataTableModel<Tutor> tutoresTableModel = createTutoresTableModel();

    private boolean esTutor = false;
    private boolean esEstudiante = true;
    private boolean esAnalista = false;

    private JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

    public ListaUsuarios() {
        initComponents();
        enableOperations(false);

        tablaUsuarios.getSelectionModel().addListSelectionListener(this::handleTableUsuariosSelectionEvent);
        tablaUsuarios.setAutoCreateRowSorter(true);

        ((DefaultTableCellRenderer) tablaUsuarios.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.LEFT);

        try {
            loadTableAndComboFiltro();
        } catch (Exception ex) {
        }
    }

    public void refreshTable() throws Exception {
        if (esTutor) {
            loadTutores();
        }

        if (esEstudiante) {
            loadEstudiantes();
        }

        if (!esEstudiante && !esTutor) {
            loadAnalistas();
        }

        ((DataTableModel) tablaUsuarios.getModel()).fireTableDataChanged();
    }

    private void enableOperations(boolean enable) {
        btnAlta.setEnabled(enable);
        btnBaja.setEnabled(enable);
        btnModificar.setEnabled(enable);
    }

    private void loadEstudiantes() throws Exception {
        EstudianteDAO estudianteDAO = BeanFactory.local().lookup("Estudiante");

        List<Estudiante> estudiantes = estudianteDAO.findAllExceptOneWithAll(Globals.getLoggedUser().getId());

        estudiantesTableModel.setListRows(estudiantes);

        tablaUsuarios.setModel(estudiantesTableModel);

        sorter = new TableRowSorter<>(estudiantesTableModel);

        tablaUsuarios.setRowSorter(sorter);
    }

    private void loadAnalistas() throws Exception {
        AnalistaDAO analistaDAO = BeanFactory.local().lookup("Analista");

        List<Analista> analistas = analistaDAO.findAllExceptOneWithAll(Globals.getLoggedUser().getId());

        analistasTableModel.setListRows(analistas);

        tablaUsuarios.setModel(analistasTableModel);

        sorter = new TableRowSorter<>(analistasTableModel);

        tablaUsuarios.setRowSorter(sorter);
    }

    private void loadTutores() throws Exception {
        TutorDAO tutorDAO = BeanFactory.local().lookup("Tutor");

        List<Tutor> tutores = tutorDAO.findAllExceptOneWithAll(Globals.getLoggedUser().getId());

        tutoresTableModel.setListRows(tutores);

        tablaUsuarios.setModel(tutoresTableModel);

        sorter = new TableRowSorter<>(tutoresTableModel);

        tablaUsuarios.setRowSorter(sorter);
    }

    public void enableBtnsAltaBaja(boolean seleccionValida) {
        if (!seleccionValida) {
            btnAlta.setEnabled(false);
            btnBaja.setEnabled(false);

            return;
        }

        Long idEstado = getSelectedUsuario().getEstado().getId();

        if (idEstado == 2L) {
            btnAlta.setEnabled(false);
            btnBaja.setEnabled(true);

            return;
        }

        if (idEstado == 3L) {
            btnAlta.setEnabled(true);
            btnBaja.setEnabled(false);
        }
    }

    public void activateOrDeactivateUsuario() throws ServicioException, Exception {
        Usuario usuario = getSelectedUsuario();

        String estado = usuario.getEstado().getNombre();

        EstadoUsuarioDAO estadoDAO = BeanFactory.local().lookup("EstadoUsuario");
        UsuarioDAO usuarioDAO = BeanFactory.local().lookup("Usuario");

        EstadoUsuario newEstado = null;

        if (estado.equalsIgnoreCase("activo")) {
            newEstado = estadoDAO.findById(3L);
        }

        if (estado.equalsIgnoreCase("sin activar") || estado.equalsIgnoreCase("eliminado")) {
            newEstado = estadoDAO.findById(2L);
        }

        if (newEstado == null) {
            return;
        }

        usuario.setEstado(newEstado);
        usuarioDAO.edit(usuario);

        ((DataTableModel) tablaUsuarios.getModel()).fireTableDataChanged();
    }

    public void handleTableUsuariosSelectionEvent(ListSelectionEvent e) {
        boolean seleccionValida = tablaUsuarios.getSelectedRow() != -1;

        btnModificar.setEnabled(seleccionValida);

        enableBtnsAltaBaja(seleccionValida);
    }

    public Usuario getSelectedUsuario() {
        int modelRow = tablaUsuarios.convertRowIndexToModel​(tablaUsuarios.getSelectedRow());
        DataTableModel model = (DataTableModel) tablaUsuarios.getModel();

        Usuario usuario = (Usuario) model.getValueAt(modelRow, -1);

        return usuario;
    }

    public void filterTable() {
        try {
            int opt = comboFiltroUsuarios.getSelectedIndex();

            String filter = txtFiltro.getText();
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + filter, opt));
        } catch (Exception e) {
        }
    }

    private void loadTableAndComboFiltro() throws Exception {
        String selectedTipoUsuario = String.valueOf(comboTipoUsuario.getSelectedItem());

        this.esTutor = selectedTipoUsuario.equalsIgnoreCase("tutores");
        this.esEstudiante = selectedTipoUsuario.equalsIgnoreCase("estudiantes");
        this.esAnalista = selectedTipoUsuario.equalsIgnoreCase("analistas");

        loadDefaultComboBoxFiltro();

        if (esTutor) {
            comboFiltroUsuarios.addItem("Área");
            comboFiltroUsuarios.addItem("Rol");

            loadTutores();
        }

        if (esEstudiante) {
            comboFiltroUsuarios.addItem("Generación");

            loadEstudiantes();
        }

        if (esAnalista) {
            loadAnalistas();
        }
    }

    private void loadDefaultComboBoxFiltro() {
        comboFiltroUsuarios.removeAllItems();

        comboFiltroUsuarios.addItem("Nombres");
        comboFiltroUsuarios.addItem("Apellidos");
        comboFiltroUsuarios.addItem("Nombre de Usuario");
        comboFiltroUsuarios.addItem("Documento");
        comboFiltroUsuarios.addItem("Email");
        comboFiltroUsuarios.addItem("Teléfono");
        comboFiltroUsuarios.addItem("Departamento");
        comboFiltroUsuarios.addItem("Localidad");
        comboFiltroUsuarios.addItem("ITR");
    }

    private DataTableModel createEstudiantesTableModel() {
        String[] COLUMNS = {"NOMBRES", "APELLIDOS", "NOMBRE DE USUARIO", "DOCUMENTO", "EMAIL", "TELEFONO", "GENERACION", "ESTADO"};

        return new DataTableModel<Estudiante>(COLUMNS) {
            @Override
            public Object getValueAt(Estudiante estudiante, int columnas) {
                return switch (columnas) {
                    case -1 ->
                        estudiante;
                    case 0 ->
                        estudiante.getNombres();
                    case 1 ->
                        estudiante.getApellidos();
                    case 2 ->
                        estudiante.getNombreUsuario();
                    case 3 ->
                        estudiante.getDocumento();
                    case 4 ->
                        estudiante.getEmail();
                    case 5 ->
                        estudiante.getTelefono();
                    case 6 ->
                        estudiante.getGeneracion();
                    case 7 ->
                        estudiante.getEstado().getNombre();
                    default ->
                        null;
                };
            }
        };
    }

    private DataTableModel createAnalistasTableModel() {
        String[] COLUMNS = {"NOMBRES", "APELLIDOS", "NOMBRE DE USUARIO", "DOCUMENTO", "EMAIL", "TELEFONO", "ESTADO"};

        return new DataTableModel<Analista>(COLUMNS) {
            @Override
            public Object getValueAt(Analista analista, int columnas) {
                return switch (columnas) {
                    case -1 ->
                        analista;
                    case 0 ->
                        analista.getNombres();
                    case 1 ->
                        analista.getApellidos();
                    case 2 ->
                        analista.getNombreUsuario();
                    case 3 ->
                        analista.getDocumento();
                    case 4 ->
                        analista.getEmail();
                    case 5 ->
                        analista.getTelefono();
                    case 6 ->
                        analista.getEstado().getNombre();
                    default ->
                        null;
                };
            }
        };
    }

    private DataTableModel createTutoresTableModel() {
        String[] COLUMNS = {"NOMBRES", "APELLIDOS", "NOMBRE DE USUARIO", "DOCUMENTO", "EMAIL", "TELEFONO", "AREA", "ROL", "ESTADO"};

        return new DataTableModel<Tutor>(COLUMNS) {
            @Override
            public Object getValueAt(Tutor tutor, int columnas) {
                return switch (columnas) {
                    case -1 ->
                        tutor;
                    case 0 ->
                        tutor.getNombres();
                    case 1 ->
                        tutor.getApellidos();
                    case 2 ->
                        tutor.getNombreUsuario();
                    case 3 ->
                        tutor.getDocumento();
                    case 4 ->
                        tutor.getEmail();
                    case 5 ->
                        tutor.getTelefono();
                    case 6 ->
                        tutor.getArea();
                    case 7 ->
                        tutor.getRol();
                    case 8 ->
                        tutor.getEstado().getNombre();
                    default ->
                        null;
                };
            }
        };
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        comboTipoUsuario = new javax.swing.JComboBox<>();
        txtFiltro = new javax.swing.JTextField();
        comboFiltroUsuarios = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaUsuarios = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        btnRefresh = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnAlta = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnBaja = new javax.swing.JButton();

        comboTipoUsuario.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        comboTipoUsuario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Estudiantes", "Tutores", "Analistas" }));
        comboTipoUsuario.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboTipoUsuarioItemStateChanged(evt);
            }
        });
        comboTipoUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboTipoUsuarioActionPerformed(evt);
            }
        });

        txtFiltro.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtFiltro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFiltroKeyReleased(evt);
            }
        });

        comboFiltroUsuarios.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        comboFiltroUsuarios.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboFiltroUsuariosItemStateChanged(evt);
            }
        });

        tablaUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tablaUsuarios.setRowHeight(30);
        tablaUsuarios.getTableHeader().setReorderingAllowed(false);
        tablaUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaUsuariosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaUsuarios);

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

        btnAlta.setText("Alta");
        btnAlta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAltaMouseClicked(evt);
            }
        });
        jToolBar1.add(btnAlta);

        btnModificar.setText("Modificar");
        btnModificar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnModificarMouseClicked(evt);
            }
        });
        jToolBar1.add(btnModificar);

        btnBaja.setText("Baja");
        btnBaja.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBajaMouseClicked(evt);
            }
        });
        jToolBar1.add(btnBaja);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(comboTipoUsuario, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(txtFiltro)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(comboFiltroUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, 0))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(comboFiltroUsuarios, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                    .addComponent(txtFiltro))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
                .addGap(12, 12, 12)
                .addComponent(comboTipoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void comboTipoUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboTipoUsuarioActionPerformed

    }//GEN-LAST:event_comboTipoUsuarioActionPerformed

    private void tablaUsuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaUsuariosMouseClicked
    }//GEN-LAST:event_tablaUsuariosMouseClicked

    private void btnAltaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAltaMouseClicked
        int confirmacion = JOptionPane.showConfirmDialog(null, "Por favor, confirme que desea realizar la alta del usuario", "Confirmar alta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            activateOrDeactivateUsuario();

            btnAlta.setEnabled(false);
        } catch (ServicioException e) {
        } catch (Exception ex) {
        }

    }//GEN-LAST:event_btnAltaMouseClicked

    private void btnBajaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBajaMouseClicked
        int confirmacion = JOptionPane.showConfirmDialog(null, "Por favor, confirme que desea realizar la baja del usuario", "Confirmar baja", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            activateOrDeactivateUsuario();

            btnBaja.setEnabled(false);
        } catch (ServicioException ex) {
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_btnBajaMouseClicked

    private void txtFiltroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltroKeyReleased
        filterTable();
    }//GEN-LAST:event_txtFiltroKeyReleased

    private void btnModificarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnModificarMouseClicked
        new ModificarUsuario(parentFrame, false, getSelectedUsuario(), null, this).setVisible(true);
    }//GEN-LAST:event_btnModificarMouseClicked

    private void comboFiltroUsuariosItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboFiltroUsuariosItemStateChanged

    }//GEN-LAST:event_comboFiltroUsuariosItemStateChanged

    private void comboTipoUsuarioItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboTipoUsuarioItemStateChanged
        try {
            loadTableAndComboFiltro();
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_comboTipoUsuarioItemStateChanged

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        try {
            refreshTable();
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_btnRefreshActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlta;
    private javax.swing.JButton btnBaja;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JComboBox<String> comboFiltroUsuarios;
    private javax.swing.JComboBox<String> comboTipoUsuario;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTable tablaUsuarios;
    private javax.swing.JTextField txtFiltro;
    // End of variables declaration//GEN-END:variables
}
