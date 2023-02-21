package com.fannog.cliente.ui.eventos;

import com.fannog.cliente.utils.BeanFactory;
import com.fannog.cliente.utils.DataTableModel;
import com.fannog.servidor.DAO.ConvocatoriaDAO;
import com.fannog.servidor.DAO.EstudianteDAO;
import com.fannog.servidor.entities.Convocatoria;
import com.fannog.servidor.entities.Estudiante;
import com.fannog.servidor.entities.Evento;
import com.fannog.servidor.exceptions.ServicioException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableRowSorter;

public class GestionConvocatoria extends javax.swing.JDialog {

    private Evento evento;
    private ListaConvocatorias listaConvocatorias;
    private TableRowSorter<DataTableModel> sorter;
    private DataTableModel<Estudiante> estudiantesTableModel = createTablaModelEstudiante();
    private DataTableModel<Convocatoria> convocatoriasTableModel = createConvocatoriaTableModel();

    public GestionConvocatoria(java.awt.Frame parent, boolean modal, Evento evento, ListaConvocatorias listaConvocatorias) {
        super(parent, modal);

        this.evento = evento;
        this.listaConvocatorias = listaConvocatorias;

        initComponents();

        tablaEstudiantes.getSelectionModel().addListSelectionListener(this::handleTableEstudianteSelection);
        tablaConvocatorias.getSelectionModel().addListSelectionListener(this::handleTableConvocatoriaSelection);

        lblEvento.setText(evento.getNombre());
        try {
            loadEstudiantes();
            loadConvocatorias();
            loadDefaultComboBoxFiltro();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadDefaultComboBoxFiltro() {
        comboFiltroUsuarios.removeAllItems();

        comboFiltroUsuarios.addItem("Nombres");
        comboFiltroUsuarios.addItem("Apellidos");
        comboFiltroUsuarios.addItem("Documento");
        comboFiltroUsuarios.addItem("Departamento");
        comboFiltroUsuarios.addItem("Localidad");
        comboFiltroUsuarios.addItem("ITR");
        comboFiltroUsuarios.addItem("Generación");
    }

    public void loadEstudiantes() throws Exception {
        EstudianteDAO estudianteDAO = BeanFactory.local().lookup("Estudiante");
        ConvocatoriaDAO convocatoriaDAO = BeanFactory.local().lookup("Convocatoria");

        List<Estudiante> estudiantes = estudianteDAO.findAllWithAll();
        List<Convocatoria> convocatorias = convocatoriaDAO.findByEvento(this.evento.getId());
        List<Estudiante> noConvocados = new ArrayList<>();

        for (Convocatoria c : convocatorias) {
            for (Estudiante e : estudiantes) {
                if (e.getId().longValue() == c.getEstudiante().getId().longValue()) {
                    noConvocados.add(e);
                }
            }
        }

        estudiantes.removeAll(noConvocados);

        estudiantesTableModel.setListRows(estudiantes);
        tablaEstudiantes.setModel(estudiantesTableModel);
        sorter = new TableRowSorter<>(estudiantesTableModel);

        tablaEstudiantes.setRowSorter(sorter);

    }

    public Estudiante getSelectedEstudiante() {
        int row = tablaEstudiantes.getSelectedRow();

        Estudiante estudiante = (Estudiante) tablaEstudiantes.getValueAt(row, -1);
        return estudiante;
    }

    public List<Estudiante> getSelectedListEstudiantes() {
        int maxRow = tablaEstudiantes.getRowCount();
        List<Estudiante> estudiantes = new LinkedList<>();

        if (maxRow != -1) {
            for (int i = 0; i < maxRow; i++) {
                Estudiante e = (Estudiante) tablaEstudiantes.getValueAt(i, -1);
                estudiantes.add(e);
            }
        }
        return estudiantes;
    }

    public Convocatoria getSelectedConvocatoria() {
        int row = tablaConvocatorias.getSelectedRow();

        Convocatoria convocatoria = (Convocatoria) tablaConvocatorias.getValueAt(row, -1);
        return convocatoria;
    }

    public List<Convocatoria> getListaConvocados() {
        int maxRow = tablaConvocatorias.getRowCount();
        List<Convocatoria> convocatorias = new LinkedList<>();

        if (maxRow != -1) {
            for (int i = 0; i < maxRow; i++) {
                Convocatoria c = (Convocatoria) tablaConvocatorias.getValueAt(i, -1);
                convocatorias.add(c);
            }
        }
        return convocatorias;
    }

    public DataTableModel createTablaModelEstudiante() {
        String[] COLUMNS = {"NOMBRES", "APELLIDOS", "DOCUMENTO", "DEPARTAMENTO", "LOCALIDAD", "ITR", "GENERACIÓN"};

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
                        estudiante.getDocumento();
                    case 3 ->
                        estudiante.getLocalidad().getDepartamento().getNombre();
                    case 4 ->
                        estudiante.getLocalidad().getNombre();
                    case 5 ->
                        estudiante.getItr().getNombre();
                    case 6 ->
                        estudiante.getGeneracion().toString();
                    default ->
                        null;
                };
            }
        };
    }

    public void loadConvocatorias() throws Exception {
        ConvocatoriaDAO convocatoriaDAO = BeanFactory.local().lookup("Convocatoria");

        List<Convocatoria> convocatorias = convocatoriaDAO.findByEvento(this.evento.getId());

        convocatoriasTableModel.setListRows(convocatorias);
        tablaConvocatorias.setModel(convocatoriasTableModel);
    }

    private DataTableModel createConvocatoriaTableModel() {
        String[] COLUMNS = {"NOMBRES", "APELLIDOS", "DOCUMENTO", "ITR", "GENERACIÓN"};

        return new DataTableModel<Convocatoria>(COLUMNS) {
            @Override
            public Object getValueAt(Convocatoria convocatoria, int columnas) {
                return switch (columnas) {
                    case -1 ->
                        convocatoria;
                    case 0 ->
                        convocatoria.getEstudiante().getNombres();
                    case 1 ->
                        convocatoria.getEstudiante().getApellidos();
                    case 2 ->
                        convocatoria.getEstudiante().getDocumento();
                    case 3 ->
                        convocatoria.getEstudiante().getItr().getNombre();
                    case 4 ->
                        convocatoria.getEstudiante().getGeneracion().toString();
                    default ->
                        null;
                };
            }
        };
    }

    public void filterTable() {
        try {
            int opt = comboFiltroUsuarios.getSelectedIndex();

            String filter = txtFiltro.getText();
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + filter, opt));
        } catch (Exception e) {
        }
    }

    public void handleTableEstudianteSelection(ListSelectionEvent e) {
        boolean seleccionValida = tablaEstudiantes.getSelectedRow() != -1;

        if (seleccionValida) {
            btnAgregar.setEnabled(true);
            return;
        }

        btnAgregar.setEnabled(false);
    }

    public void handleTableConvocatoriaSelection(ListSelectionEvent e) {
        boolean seleccionValida = tablaConvocatorias.getSelectedRow() != -1;

        if (seleccionValida) {
            btnQuitar.setEnabled(true);
            return;
        }

        btnQuitar.setEnabled(false);
    }

    public void agregarEstudiante() throws ServicioException, Exception {
        Estudiante estudiante = getSelectedEstudiante();

        ConvocatoriaDAO convocatoriaDAO = BeanFactory.local().lookup("Convocatoria");
        Convocatoria convocatoria = new Convocatoria("Pendiente", this.evento, estudiante);
        Convocatoria newConvocatoria = convocatoriaDAO.create(convocatoria);

        convocatoriasTableModel.getListRows().add(newConvocatoria);
        convocatoriasTableModel.fireTableDataChanged();

        estudiantesTableModel.getListRows().remove(estudiante);
        estudiantesTableModel.fireTableDataChanged();

        listaConvocatorias.getConvocatoriaTableModel().getListRows().add(newConvocatoria);
        listaConvocatorias.getConvocatoriaTableModel().fireTableDataChanged();
    }

    public void agregarTablaDeEstudiantes() throws Exception {
        List<Estudiante> estudiantes = getSelectedListEstudiantes();
        ConvocatoriaDAO convocatoriaDAO = BeanFactory.local().lookup("Convocatoria");

        for (Estudiante e : estudiantes) {
            Convocatoria convocatoria = new Convocatoria("Pendiente", this.evento, e);
            Convocatoria newConvocatoria = convocatoriaDAO.create(convocatoria);

            convocatoriasTableModel.getListRows().add(newConvocatoria);
            convocatoriasTableModel.fireTableDataChanged();

            estudiantesTableModel.getListRows().remove(e);
            estudiantesTableModel.fireTableDataChanged();

            listaConvocatorias.getConvocatoriaTableModel().getListRows().add(newConvocatoria);
            listaConvocatorias.getConvocatoriaTableModel().fireTableDataChanged();
        }
    }

    public void quitarEstudiante() throws ServicioException, Exception {
        ConvocatoriaDAO convocatoriaDAO = BeanFactory.local().lookup("Convocatoria");
        EstudianteDAO estudianteDAO = BeanFactory.local().lookup("Estudiante");

        Convocatoria convocatoria = getSelectedConvocatoria();
        Estudiante estudiante = estudianteDAO.findById(convocatoria.getEstudiante().getId());

        convocatoriaDAO.remove(convocatoria);

        estudiantesTableModel.getListRows().add(estudiante);
        estudiantesTableModel.fireTableDataChanged();

        convocatoriasTableModel.getListRows().remove(convocatoria);
        convocatoriasTableModel.fireTableDataChanged();

        listaConvocatorias.getConvocatoriaTableModel().getListRows().remove(convocatoria);
        listaConvocatorias.getConvocatoriaTableModel().fireTableDataChanged();
    }

    public void quitarTodosConvocados() throws Exception {
        List<Convocatoria> convocatorias = getListaConvocados();
        ConvocatoriaDAO convocatoriaDAO = BeanFactory.local().lookup("Convocatoria");

        for (Convocatoria c : convocatorias) {
            convocatoriaDAO.remove(c);

            convocatoriasTableModel.getListRows().remove(c);
            convocatoriasTableModel.fireTableDataChanged();

            listaConvocatorias.getConvocatoriaTableModel().getListRows().remove(c);
            listaConvocatorias.getConvocatoriaTableModel().fireTableDataChanged();

            estudiantesTableModel.getListRows().add(c.getEstudiante());
            estudiantesTableModel.fireTableDataChanged();
        }

        listaConvocatorias.getConvocatoriaTableModel().fireTableDataChanged();

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtFiltro = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        comboFiltroUsuarios = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaEstudiantes = new javax.swing.JTable();
        lblEvento = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaConvocatorias = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        btnQuitar = new javax.swing.JButton();
        btnRemoveAll = new javax.swing.JButton();
        btnAddAll = new javax.swing.JButton();
        btnAgregar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Fannog - Modificar convocatoria");

        jLabel1.setText("Listado de estudiantes para evento:");

        txtFiltro.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtFiltro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFiltroKeyReleased(evt);
            }
        });

        jLabel3.setText("Filtro:");

        comboFiltroUsuarios.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        tablaEstudiantes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tablaEstudiantes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaEstudiantesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaEstudiantes);

        lblEvento.setText("jLabel2");

        tablaConvocatorias.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tablaConvocatorias.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaConvocatoriasMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tablaConvocatorias);

        jLabel2.setText("Estudiantes no registrados");

        btnQuitar.setText("Quitar");
        btnQuitar.setEnabled(false);
        btnQuitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitarActionPerformed(evt);
            }
        });

        btnRemoveAll.setText("Quitar todos");
        btnRemoveAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveAllActionPerformed(evt);
            }
        });

        btnAddAll.setText("Agregar todos");
        btnAddAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddAllActionPerformed(evt);
            }
        });

        btnAgregar.setText("Agregar");
        btnAgregar.setEnabled(false);
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRemoveAll, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnQuitar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnAddAll, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(lblEvento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 803, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtFiltro)
                                .addGap(12, 12, 12)
                                .addComponent(comboFiltroUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(12, 12, 12))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEvento, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboFiltroUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnAddAll, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                    .addComponent(btnAgregar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnQuitar, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                    .addComponent(btnRemoveAll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(12, 12, 12))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void tablaEstudiantesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaEstudiantesMouseClicked

    }//GEN-LAST:event_tablaEstudiantesMouseClicked

    private void tablaConvocatoriasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaConvocatoriasMouseClicked
    }//GEN-LAST:event_tablaConvocatoriasMouseClicked

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        try {
            agregarEstudiante();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnQuitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitarActionPerformed
        try {
            quitarEstudiante();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnQuitarActionPerformed

    private void btnAddAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddAllActionPerformed
        try {
            agregarTablaDeEstudiantes();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnAddAllActionPerformed

    private void btnRemoveAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveAllActionPerformed
        try {
            quitarTodosConvocados();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnRemoveAllActionPerformed

    private void txtFiltroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltroKeyReleased
        filterTable();
    }//GEN-LAST:event_txtFiltroKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddAll;
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnQuitar;
    private javax.swing.JButton btnRemoveAll;
    private javax.swing.JComboBox<String> comboFiltroUsuarios;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblEvento;
    private javax.swing.JTable tablaConvocatorias;
    private javax.swing.JTable tablaEstudiantes;
    private javax.swing.JTextField txtFiltro;
    // End of variables declaration//GEN-END:variables
}
