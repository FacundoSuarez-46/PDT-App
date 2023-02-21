package com.fannog.cliente.ui.estudiante;

import com.fannog.cliente.utils.BeanFactory;
import com.fannog.cliente.utils.Globals;
import com.fannog.servidor.DAO.AdjuntoSolicitudDAO;
import com.fannog.servidor.DAO.ConvocatoriaDAO;
import com.fannog.servidor.DAO.EstadoSolicitudDAO;
import com.fannog.servidor.DAO.EventoDAO;
import com.fannog.servidor.DAO.SolicitudDAO;
import com.fannog.servidor.DAO.TipoConstanciaDAO;
import com.fannog.servidor.entities.AdjuntoSolicitud;
import com.fannog.servidor.entities.Convocatoria;
import com.fannog.servidor.entities.EstadoSolicitud;
import com.fannog.servidor.entities.Estudiante;
import com.fannog.servidor.entities.Evento;
import com.fannog.servidor.entities.Solicitud;
import com.fannog.servidor.entities.TipoConstancia;
import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class NuevaSolicitud extends javax.swing.JDialog {

    JFileChooser chooser;
    ListadoSolicitudes panel;

    List<File> listFiles = new ArrayList<>();

    DefaultListModel listModel = new DefaultListModel();

    public NuevaSolicitud(java.awt.Frame parent, boolean modal, ListadoSolicitudes panel) {
        super(parent, modal);
        this.panel = panel;

        initComponents();
        myComponentsInit();
    }

    private void myComponentsInit() {
        try {
            cleanList();

            populateComboEventos();
            populateComboTipoConstancias();
        } catch (Exception ex) {
        }
    }

    private void populateComboTipoConstancias() throws Exception {
        TipoConstanciaDAO tipoConstDAO = BeanFactory.local().lookup("TipoConstancia");
        List<TipoConstancia> tipoConstancias = tipoConstDAO.findAll();
        comboTipoConstancia.setModel(new DefaultComboBoxModel(tipoConstancias.toArray()));
    }

    private void populateComboEventos() throws Exception {
        EventoDAO eventoDAO = BeanFactory.local().lookup("Evento");
        List<Evento> eventos = eventoDAO.findAll();
        List<Evento> eventosAsistidos = new ArrayList<>();

        String asistencia;

        for (Evento e : eventos) {
            List<Convocatoria> convocatorias = e.getConvocatorias();
            for (Convocatoria c : convocatorias) {
                asistencia = c.getAsistencia();
                System.out.println("asistencia: " + asistencia);
                if (asistencia.startsWith("ASISTENCIA") && c.getEstudiante().getId() == Globals.getLoggedUser().getId()) {
                    eventosAsistidos.add(e);
                }
            }
        }

        if (eventosAsistidos.isEmpty()) {
            comboEvento.setModel(new DefaultComboBoxModel<>());
            comboEvento.addItem("No tienes asistencias");
            btnAddSolicitud.setToolTipText("No puedes solicitar porque no asististe a ningun evento");
            btnAddSolicitud.setEnabled(false);
            btnAdjuntos.setEnabled(false);
            btnEliminarAdjunto.setEnabled(false);
            btnEliminarAdjuntos.setEnabled(false);
            listAdjuntos.setEnabled(false);
            fieldDetalle.setEnabled(false);
            comboTipoConstancia.setEnabled(false);
            comboEvento.setEnabled(false);
            JOptionPane.showMessageDialog(this, "No es posible solicitar una constancia, no asististe a ningun evento!", "Atencion!", 0);
        } else {
            comboEvento.setModel(new DefaultComboBoxModel(eventosAsistidos.toArray()));
        }
    }

    public DefaultListModel cleanList() {
        listAdjuntos.setModel(listModel);
        listModel.addElement("No hay adjuntos");

        return listModel;
    }

    private void refreshNewAdjuntos() {
        for (File f : listFiles) {
            listModel.addElement(f);
        }
        listModel.removeElement("No hay adjuntos");
    }

    private void removeAllAdjuntos() {
        listModel.clear();
        listFiles.clear();
    }

    private void removeSelectedAdjuntos() {
        for (File f : listAdjuntos.getSelectedValuesList()) {
            listModel.removeElement(f);
            listFiles.remove(f);
        }
    }

    private void addSolicitud(List<File> files) {
        try {
            SolicitudDAO solicitudDAO = BeanFactory.local().lookup("Solicitud");
            EstadoSolicitudDAO estadoDAO = BeanFactory.local().lookup("EstadoSolicitud");
            Estudiante estudiante = (Estudiante) Globals.getLoggedUser();
            EstadoSolicitud estado = estadoDAO.findById(1L);
            TipoConstancia tipoConst = (TipoConstancia) comboTipoConstancia.getSelectedItem();
            Evento evento = (Evento) comboEvento.getSelectedItem();
            String detalle = fieldDetalle.getText();

            Solicitud solicitud = new Solicitud(detalle, estado, estudiante, tipoConst, evento);

            solicitudDAO.create(solicitud, files);
            panel.refreshTable();
            dispose();
        } catch (Exception e) {
        }
    }

    private void handleChooser() {
        chooser = new JFileChooser();
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("*.pdf", "pdf"));
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("*.doc", "doc"));
        chooser.setMultiSelectionEnabled(true);

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

            File[] fileArray;
            fileArray = chooser.getSelectedFiles();

            listFiles.addAll(Arrays.asList(fileArray));
            refreshNewAdjuntos();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        comboTipoConstancia = new javax.swing.JComboBox<>();
        comboEvento = new javax.swing.JComboBox<>();
        btnAddSolicitud = new javax.swing.JButton();
        btnAdjuntos = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        fieldDetalle = new javax.swing.JTextArea();
        btnCancelar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        listAdjuntos = new javax.swing.JList<>();
        btnEliminarAdjunto = new javax.swing.JButton();
        btnEliminarAdjuntos = new javax.swing.JButton();
        countChars = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Fannog - Nueva solicitud");

        jLabel1.setText("Tipo constancia");

        jLabel2.setText("Evento");

        jLabel3.setText("Detalle");

        jLabel4.setText("Adjuntos");

        comboTipoConstancia.setToolTipText("Tipo de constancia que solicita");
        comboTipoConstancia.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof TipoConstancia) {
                    TipoConstancia tipoConstancia = (TipoConstancia) value;
                    setText(tipoConstancia.getNombre());
                }
                return this;
            }
        });

        comboEvento.setToolTipText("Seleccionar evento de la solicitud de constancia");
        comboEvento.setRenderer(new DefaultListCellRenderer() {
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

        btnAddSolicitud.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAddSolicitud.setText("Solicitar");
        btnAddSolicitud.setToolTipText("Solicita una nueva constancia");
        btnAddSolicitud.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddSolicitudActionPerformed(evt);
            }
        });

        btnAdjuntos.setText("Subir");
        btnAdjuntos.setToolTipText("Sube adjuntos a tu solicitud de constancia");
        btnAdjuntos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdjuntosActionPerformed(evt);
            }
        });

        fieldDetalle.setColumns(20);
        fieldDetalle.setLineWrap(true);
        fieldDetalle.setRows(5);
        fieldDetalle.setToolTipText("Detalle de la solicitud");
        fieldDetalle.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                fieldDetalleKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(fieldDetalle);

        btnCancelar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.setToolTipText("Cancela la solicitud de constancia");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        listAdjuntos.setToolTipText("Adjuntos a cargar");
        listAdjuntos.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value instanceof File) {
                    File f = (File) value;
                    setText(f.getName());
                }
                return this;
            }
        }
    );
    jScrollPane2.setViewportView(listAdjuntos);

    btnEliminarAdjunto.setText("Eliminar");
    btnEliminarAdjunto.setToolTipText("Elimina los adjuntos seleccionados");
    btnEliminarAdjunto.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnEliminarAdjuntoActionPerformed(evt);
        }
    });

    btnEliminarAdjuntos.setText("Eliminar Todos");
    btnEliminarAdjuntos.setToolTipText("Elimina todos los adjuntos");
    btnEliminarAdjuntos.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnEliminarAdjuntosActionPerformed(evt);
        }
    });

    countChars.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
    countChars.setText("0/1000");
    countChars.setToolTipText("");

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addGap(12, 12, 12)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3)
                            .addComponent(comboTipoConstancia, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(264, 264, 264))
                            .addComponent(comboEvento, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(countChars)
                                .addGap(11, 11, 11))))
                    .addComponent(btnAdjuntos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btnEliminarAdjunto, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnEliminarAdjuntos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(24, 24, 24)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnAddSolicitud, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addGap(12, 12, 12)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel1)
                .addComponent(jLabel2))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(comboTipoConstancia, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(comboEvento, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel3)
                .addComponent(countChars))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(jLabel4)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(btnAdjuntos, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEliminarAdjunto)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(btnEliminarAdjuntos)
                    .addGap(71, 71, 71))
                .addGroup(layout.createSequentialGroup()
                    .addGap(21, 21, 21)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(35, 35, 35)
                            .addComponent(btnAddSolicitud, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap(24, Short.MAX_VALUE))))
    );

    pack();
    setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddSolicitudActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddSolicitudActionPerformed
        addSolicitud(listFiles);
    }//GEN-LAST:event_btnAddSolicitudActionPerformed

    private void btnAdjuntosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdjuntosActionPerformed
        handleChooser();
    }//GEN-LAST:event_btnAdjuntosActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnEliminarAdjuntoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarAdjuntoActionPerformed
        removeSelectedAdjuntos();
    }//GEN-LAST:event_btnEliminarAdjuntoActionPerformed

    private void btnEliminarAdjuntosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarAdjuntosActionPerformed
        removeAllAdjuntos();
    }//GEN-LAST:event_btnEliminarAdjuntosActionPerformed

    private void fieldDetalleKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fieldDetalleKeyTyped
        int max = 999;
        String length = String.valueOf(fieldDetalle.getText().length() + 1);
        countChars.setText(length + "/1000");

        if (fieldDetalle.getText().length() >= 600 && fieldDetalle.getText().length() < 999) {
            countChars.setForeground(Color.red);
        } else if (fieldDetalle.getText().length() < 600 && fieldDetalle.getText().length() >= 300) {
            countChars.setForeground(Color.yellow);
        } else if (fieldDetalle.getText().length() == 999) {
            countChars.setText("Maximo numero de caracteres!");
            countChars.setForeground(Color.red);
        } else {
            countChars.setForeground(Color.gray);
        }

        if (fieldDetalle.getText().length() > max + 1) {
            evt.consume();
            String shortened = fieldDetalle.getText().substring(0, max);
            fieldDetalle.setText(shortened);
        } else if (fieldDetalle.getText().length() >= max) {
            evt.consume();
        }
    }//GEN-LAST:event_fieldDetalleKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddSolicitud;
    private javax.swing.JButton btnAdjuntos;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEliminarAdjunto;
    private javax.swing.JButton btnEliminarAdjuntos;
    private javax.swing.JComboBox<String> comboEvento;
    private javax.swing.JComboBox<String> comboTipoConstancia;
    private javax.swing.JLabel countChars;
    private javax.swing.JTextArea fieldDetalle;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList<File> listAdjuntos;
    // End of variables declaration//GEN-END:variables
}
