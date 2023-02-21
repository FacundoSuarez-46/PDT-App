package com.fannog.cliente.ui.eventos;

import com.fannog.cliente.utils.BeanFactory;
import com.fannog.cliente.utils.DataTableModel;
import com.fannog.cliente.utils.FieldUtils;
import com.fannog.servidor.DAO.ConvocatoriaDAO;
import com.fannog.servidor.DAO.EventoDAO;
import com.fannog.servidor.entities.Convocatoria;
import com.fannog.servidor.entities.Evento;
import java.awt.Component;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;

public class ListaConvocatorias extends javax.swing.JPanel {

    private JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

    private DataTableModel<Convocatoria> convocatoriaTableModel = createConvocatoriaTableModel();

    public ListaConvocatorias() {
        initComponents();

        showAndEnableForm(false);

        tablaConvocatorias.getSelectionModel().addListSelectionListener(this::handleTableConvocatoriaSelection);

        try {
            populateComboEvento();
            loadConvocatorias();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public DataTableModel<Convocatoria> getConvocatoriaTableModel() {
        return convocatoriaTableModel;
    }

    public void populateComboEvento() throws Exception {
        EventoDAO eventoDAO = BeanFactory.local().lookup("Evento");

        List<Evento> eventos = eventoDAO.findAll();

        comboEventos.setModel(new DefaultComboBoxModel(eventos.toArray()));
    }

    public Evento getSelectedEvento() {
        Evento evento = (Evento) comboEventos.getSelectedItem();

        return evento;
    }

    public Convocatoria getSelectedConvocatoria() {
        int row = tablaConvocatorias.getSelectedRow();

        Convocatoria convocatoria = (Convocatoria) tablaConvocatorias.getValueAt(row, -1);
        return convocatoria;
    }

    public void loadConvocatorias() throws Exception {
        Evento evento = getSelectedEvento();

        ConvocatoriaDAO convocatoriaDAO = BeanFactory.local().lookup("Convocatoria");

        List<Convocatoria> convocatorias = convocatoriaDAO.findByEvento(evento.getId());

        convocatoriaTableModel.setListRows(convocatorias);
        tablaConvocatorias.setModel(convocatoriaTableModel);
    }

    public void handleTableConvocatoriaSelection(ListSelectionEvent e) {
        boolean seleccionValida = tablaConvocatorias.getSelectedRow() != -1;

        showAndEnableForm(seleccionValida);
    }

    private void showAndEnableForm(boolean show) {
        form.setEnabled(show);
        form.setVisible(show);
    }

    private DataTableModel createConvocatoriaTableModel() {
        String[] COLUMNS = {"NOMBRES", "APELLIDOS", "DOCUMENTO", "ITR", "ASISTENCIA", "CALIFICACIÓN"};

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
                        convocatoria.getAsistencia();
                    case 5 ->
                        convocatoria.getCalificacion();
                    default ->
                        null;
                };
            }
        };
    }

    public void verConvocatoria() {
        Convocatoria c = getSelectedConvocatoria();

        lblNombre.setText(c.getEstudiante().getNombres());
        lblApellido.setText(c.getEstudiante().getApellidos());
        comboAsistencia.getModel().setSelectedItem(c.getAsistencia());

        if (c.getCalificacion() == null) {
            txtNota.setText("");
        } else {
            txtNota.setText(c.getCalificacion());
        }
    }

    public void editarConvocatoria() throws Exception {
        Convocatoria c = getSelectedConvocatoria();
        String asistencia = (String) comboAsistencia.getSelectedItem();
        String notaS = txtNota.getText().trim();

        c.setAsistencia(asistencia);
        if (!notaS.contains(",")) {

            if (notaS.isBlank()) {

                c.setCalificacion("");
            } else {

                double nota = Double.parseDouble(notaS);

                if (nota >= 1 && nota <= 5) {
                    if (notaS.length() <= 5) {
                        c.setCalificacion(notaS);
                    } else {
                        JOptionPane.showMessageDialog(parentFrame, "La calificación debe tener maximo 3 decimales", "Califiación Incorrecta", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(parentFrame, "La calificación debe quedar vacía o estar entre 1 y 5", "Califiación Incorrecta", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(parentFrame, "Los decimales deben ser indicados con punto, EJ: 3.5", "Califiación Incorrecta", JOptionPane.ERROR_MESSAGE);
        }

        ConvocatoriaDAO convocatoriaDAO = BeanFactory.local().lookup("Convocatoria");
        convocatoriaDAO.edit(c);
        convocatoriaTableModel.fireTableDataChanged();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tablaConvocatorias = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        comboEventos = new javax.swing.JComboBox<>();
        btnConvocatoria = new javax.swing.JButton();
        form = new javax.swing.JPanel();
        lblNombre = new javax.swing.JLabel();
        lblApellido = new javax.swing.JLabel();
        lblAsistencia = new javax.swing.JLabel();
        comboAsistencia = new javax.swing.JComboBox<>();
        lblNota = new javax.swing.JLabel();
        txtNota = new javax.swing.JTextField();
        btnActualizar = new javax.swing.JButton();

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
        jScrollPane1.setViewportView(tablaConvocatorias);

        jLabel2.setText("Evento");

        comboEventos.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboEventosItemStateChanged(evt);
            }
        });
        comboEventos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboEventosActionPerformed(evt);
            }
        });
        comboEventos.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Evento) {
                    Evento evento = (Evento) value;
                    setText(evento.getNombre());
                }
                return this;
            }
        });

        btnConvocatoria.setText("Editar Convocatoria");
        btnConvocatoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConvocatoriaActionPerformed(evt);
            }
        });

        lblNombre.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblNombre.setText("Nombre Est");

        lblApellido.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblApellido.setText("Apellido Est");

        lblAsistencia.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblAsistencia.setText("Asistencia:");

        comboAsistencia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "PENDIENTE", "ASISTENCIA", "ASISTENCIA MATUTINA", "ASISTENCIA VESPERTINA", "AUSENCIA", "AUSENCIA JUSTIFICADA" }));

        lblNota.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblNota.setText("Calificación:");

        txtNota.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNotaKeyTyped(evt);
            }
        });

        btnActualizar.setText("Actualizar");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout formLayout = new javax.swing.GroupLayout(form);
        form.setLayout(formLayout);
        formLayout.setHorizontalGroup(
            formLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
            .addComponent(lblApellido, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
            .addGroup(formLayout.createSequentialGroup()
                .addGroup(formLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lblNota, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
                    .addComponent(lblAsistencia, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(formLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtNota)
                    .addComponent(comboAsistencia, 0, 180, Short.MAX_VALUE)))
            .addComponent(btnActualizar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        formLayout.setVerticalGroup(
            formLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formLayout.createSequentialGroup()
                .addComponent(lblNombre)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblApellido)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(formLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAsistencia, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboAsistencia, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(formLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNota)
                    .addComponent(txtNota, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(comboEventos, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnConvocatoria, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 583, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(form, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(12, 12, 12)
                .addComponent(comboEventos, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE)
                    .addComponent(form, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnConvocatoria, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tablaConvocatoriasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaConvocatoriasMouseClicked
        verConvocatoria();
    }//GEN-LAST:event_tablaConvocatoriasMouseClicked

    private void btnConvocatoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConvocatoriaActionPerformed
        new GestionConvocatoria(parentFrame, true, getSelectedEvento(), this).show();
    }//GEN-LAST:event_btnConvocatoriaActionPerformed

    private void comboEventosItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboEventosItemStateChanged
        try {
            loadConvocatorias();
            convocatoriaTableModel.fireTableDataChanged();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_comboEventosItemStateChanged

    private void comboEventosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboEventosActionPerformed
    }//GEN-LAST:event_comboEventosActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        try {
            editarConvocatoria();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void txtNotaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNotaKeyTyped
        FieldUtils.esNumero(evt);
    }//GEN-LAST:event_txtNotaKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnConvocatoria;
    private javax.swing.JComboBox<String> comboAsistencia;
    private javax.swing.JComboBox<String> comboEventos;
    private javax.swing.JPanel form;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblApellido;
    private javax.swing.JLabel lblAsistencia;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblNota;
    private javax.swing.JTable tablaConvocatorias;
    private javax.swing.JTextField txtNota;
    // End of variables declaration//GEN-END:variables
}
