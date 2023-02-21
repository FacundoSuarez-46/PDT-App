package com.fannog.cliente.ui.estudiante;

import com.fannog.cliente.utils.BeanFactory;
import com.fannog.cliente.utils.Globals;
import com.fannog.servidor.DAO.AdjuntoSolicitudDAO;
import com.fannog.servidor.DAO.EstadoSolicitudDAO;
import com.fannog.servidor.DAO.EventoDAO;
import com.fannog.servidor.DAO.SolicitudDAO;
import com.fannog.servidor.DAO.TipoConstanciaDAO;
import com.fannog.servidor.DAO.impl.AdjuntoSolicitudDAOImpl;
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
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class EditarSolicitud extends javax.swing.JDialog {

    ListadoSolicitudes panel;
    FichaSolicitud ficha;
    Solicitud solicitud;

    JFileChooser chooser;

    List<File> listFiles = new ArrayList<>();
    List<AdjuntoSolicitud> filesToDelete;
    List<AdjuntoSolicitud> adjuntos = new ArrayList<>();

    DefaultListModel listModel = new DefaultListModel();
    DefaultListModel listNewModel = new DefaultListModel();

    public EditarSolicitud(java.awt.Frame parent, boolean modal, ListadoSolicitudes panel) {
        super(parent, modal);
        this.filesToDelete = new ArrayList<AdjuntoSolicitud>();
        this.panel = panel;
        this.ficha = ficha;

        initComponents();
        myComponentsInit();
    }

    private void myComponentsInit() {
        try {
            AdjuntoSolicitudDAO adjuntoSolicitudDAO = BeanFactory.local().lookup("AdjuntoSolicitud");

            cleanList();
            cleanNewList();
            setSolicitud(panel.getSelectedSolicitud());
            setAdjuntos(adjuntoSolicitudDAO.findAllBySolicitud(solicitud.getId()));

            populateComboEventos();
            populateComboTipoConstancias();
            loadAdjuntos();
            checkBtnAdjuntos();
            checkBtnNewAdjuntos();

            comboEvento.getModel().setSelectedItem(solicitud.getEvento());
            comboTipoConstancia.getModel().setSelectedItem(solicitud.getTipo());
            fieldDetalle.setText(solicitud.getDetalle());
            btnEliminarAdjunto.setEnabled(false);
            btnEliminarNewAdjunto.setEnabled(false);

            String length = String.valueOf(fieldDetalle.getText().length() + 1);
            countChars.setText(length + "/1000");
        } catch (Exception ex) {
            Logger.getLogger(EditarSolicitud.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadAdjuntos() {
        listModel = (DefaultListModel) listAdjuntos.getModel();

        for (AdjuntoSolicitud a : adjuntos) {
            if (!a.isEliminado()) {
                listModel.addElement(a);
            }
        }

        if (listModel.isEmpty()) {
            listModel.addElement("No hay adjuntos");
        }
    }

    private void removeAdjunto() {
        listModel = (DefaultListModel) listAdjuntos.getModel();
        List<AdjuntoSolicitud> adjuntosSeleccionados = listAdjuntos.getSelectedValuesList();
        filesToDelete.addAll(adjuntosSeleccionados);

        for (AdjuntoSolicitud a : adjuntosSeleccionados) {
            listModel.removeElement(a);
        }
    }

    private void removeAllAdjuntos() {
        listModel.removeAllElements();
        filesToDelete = adjuntos;
        listModel.addElement("No hay adjuntos");
        checkBtnAdjuntos();
    }

    //NEW ADJUNTOS NEW ADJUNTOS
    private void refreshNewAdjuntos() {
        for (File f : listFiles) {
            listNewModel.addElement(f);
        }

        listNewModel.removeElement("No hay nuevos adjuntos");
        checkBtnNewAdjuntos();
    }

    private void removeSelectedNewAdjuntos() {
        for (File f : listNewAdjuntos.getSelectedValuesList()) {
            listNewModel.removeElement(f);
            listFiles.remove(f);
        }
    }

    private void removeAllNewAdjuntos() {
        listNewModel.clear();
        listFiles.clear();
        listNewModel.addElement("No hay nuevos adjuntos");
    }

    private void addAdjunto() throws Exception {
        AdjuntoSolicitudDAO adjuntoSolicitudDAO = BeanFactory.local().lookup("AdjuntoSolicitud");

        for (File f : listFiles) {
            adjuntoSolicitudDAO.create(f, solicitud);
        }
    }

    private void deleteAdjuntos() throws Exception {
        AdjuntoSolicitudDAO adjuntoSolicitudDAO = BeanFactory.local().lookup("AdjuntoSolicitud");

        adjuntoSolicitudDAO.remove(filesToDelete);
    }

    private void editSolicitud() {
        try {
            SolicitudDAO solicitudDAO = BeanFactory.local().lookup("Solicitud");

            Estudiante estudiante = (Estudiante) Globals.getLoggedUser();
            EstadoSolicitud estado = panel.getSelectedSolicitud().getEstado();
            TipoConstancia tipoConst = (TipoConstancia) comboTipoConstancia.getSelectedItem();
            Evento evento = (Evento) comboEvento.getSelectedItem();
            String detalle = fieldDetalle.getText();

            solicitud.setEstudiante(estudiante);
            solicitud.setEstado(estado);
            solicitud.setTipo(tipoConst);
            solicitud.setEvento(evento);
            solicitud.setDetalle(detalle);

            Solicitud updateSolicitud = getSolicitud();
            solicitudDAO.edit(updateSolicitud);
            panel.refreshTable();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(rootPane, "No se pudo editar la solicitud!");
        }
    }

    private boolean solicitudEsIgual() {

        TipoConstancia tipoConst = (TipoConstancia) comboTipoConstancia.getSelectedItem();
        Evento evento = (Evento) comboEvento.getSelectedItem();
        String detalle = fieldDetalle.getText();

        if (tipoConst == solicitud.getTipo() && evento == solicitud.getEvento() && detalle == solicitud.getDetalle()) {
            return true;
        } else {
            return false;
        }
    }

    public DefaultListModel cleanList() {
        listAdjuntos.setModel(listModel);

        return listModel;
    }

    public DefaultListModel cleanNewList() {
        listNewAdjuntos.setModel(listNewModel);
        listNewModel.addElement("No hay nuevos adjuntos");

        return listNewModel;
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
        comboEvento.setModel(new DefaultComboBoxModel(eventosAsistidos.toArray()));
    }

    private void checkBtnAdjuntos() {

        if (listModel.getElementAt(0).equals("No hay adjuntos")) {
            btnEliminarAdjuntos.setEnabled(false);
            btnEliminarAdjunto.setEnabled(false);
        }
    }

    private void checkBtnNewAdjuntos() {
        if (listNewModel.getElementAt(0).equals("No hay nuevos adjuntos")) {
            btnEliminarNewAdjuntos.setEnabled(false);
            btnEliminarNewAdjunto.setEnabled(false);
        } else {
            btnEliminarNewAdjuntos.setEnabled(true);
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

    //Getters / Setters - Getters / Setters - Getters / Setters - Getters / Setters
    public Solicitud getSolicitud() {
        return solicitud;
    }

    private void setSolicitud(Solicitud solicitud) {
        this.solicitud = solicitud;
    }

    public List<AdjuntoSolicitud> getAdjuntos() {
        return adjuntos;
    }

    private void setAdjuntos(List<AdjuntoSolicitud> adjuntos) {
        this.adjuntos = adjuntos;
    }

    //Getters / Setters - Getters / Setters - Getters / Setters - Getters / Setters
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        comboTipoConstancia = new javax.swing.JComboBox<>();
        comboEvento = new javax.swing.JComboBox<>();
        btnSave = new javax.swing.JButton();
        btnAdjuntos = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        fieldDetalle = new javax.swing.JTextArea();
        btnEliminarAdjunto = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        listAdjuntos = new javax.swing.JList<>();
        btnCancelar = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        listNewAdjuntos = new javax.swing.JList<>();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btnEliminarAdjuntos = new javax.swing.JButton();
        btnEliminarNewAdjuntos = new javax.swing.JButton();
        btnEliminarNewAdjunto = new javax.swing.JButton();
        countChars = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Fannog - Editar solicitud");

        jLabel1.setText("Tipo Constancia");

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

        btnSave.setText("Guardar");
        btnSave.setToolTipText("Guardar cambios");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
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

        btnEliminarAdjunto.setText("Eliminar");
        btnEliminarAdjunto.setToolTipText("Eliminar tus adjuntos seleccionados");
        btnEliminarAdjunto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarAdjuntoActionPerformed(evt);
            }
        });

        listAdjuntos.setToolTipText("Tus adjuntos ya cargados");
        listAdjuntos.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                listAdjuntosFocusGained(evt);
            }
        });
        listAdjuntos.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value instanceof AdjuntoSolicitud) {
                    AdjuntoSolicitud a = (AdjuntoSolicitud) value;
                    setText(a.getNombArchivo());
                }
                return this;
            }
        }
    );
    jScrollPane2.setViewportView(listAdjuntos);

    btnCancelar.setText("Cancelar");
    btnCancelar.setToolTipText("Cancelar cambios");
    btnCancelar.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnCancelarActionPerformed(evt);
        }
    });

    listNewAdjuntos.setToolTipText("Nuevos adjuntos a cargar");
    listNewAdjuntos.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusGained(java.awt.event.FocusEvent evt) {
            listNewAdjuntosFocusGained(evt);
        }
    });
    listNewAdjuntos.setCellRenderer(new DefaultListCellRenderer() {
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
    jScrollPane3.setViewportView(listNewAdjuntos);

    jLabel5.setText("Nuevos adjuntos");

    jLabel6.setText("Tus Adjuntos");

    btnEliminarAdjuntos.setText("E Todo");
    btnEliminarAdjuntos.setToolTipText("Eliminar todos Tus adjuntos");
    btnEliminarAdjuntos.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnEliminarAdjuntosActionPerformed(evt);
        }
    });

    btnEliminarNewAdjuntos.setText("E Todo");
    btnEliminarNewAdjuntos.setToolTipText("Eliminar todos los nuevos adjuntos");
    btnEliminarNewAdjuntos.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnEliminarNewAdjuntosActionPerformed(evt);
        }
    });

    btnEliminarNewAdjunto.setText("Eliminar");
    btnEliminarNewAdjunto.setToolTipText("Eliminar nuevos adjuntos seleccionados");
    btnEliminarNewAdjunto.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnEliminarNewAdjuntoActionPerformed(evt);
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
                    .addComponent(btnAdjuntos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3)
                            .addComponent(comboTipoConstancia, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel2)
                                    .addGap(264, 264, 264))
                                .addComponent(comboEvento, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(countChars)
                                .addGap(10, 10, 10)))))
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btnEliminarAdjuntos, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel6)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnEliminarAdjunto, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGap(62, 62, 62)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane3)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(btnEliminarNewAdjuntos, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnEliminarNewAdjunto, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5))
                        .addComponent(btnSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGap(12, 12, 12))
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addContainerGap()
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
            .addGap(18, 18, 18)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(18, 18, 18)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel6)
                        .addComponent(btnEliminarAdjunto))
                    .addGap(18, 18, 18)
                    .addComponent(btnEliminarAdjuntos))
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnEliminarNewAdjunto)
                        .addComponent(jLabel5))
                    .addGap(18, 18, 18)
                    .addComponent(btnEliminarNewAdjuntos)))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap())
    );

    pack();
    setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        if (JOptionPane.showConfirmDialog(this,
                "¿Estas seguro que quieres editar esta solicitud?", "Editar Solicitud", JOptionPane.YES_NO_OPTION, 3) == JOptionPane.YES_OPTION) {

            try {

                if (solicitudEsIgual() != true) {
                    editSolicitud();
                }
                if (!listFiles.isEmpty()) {
                    addAdjunto();
                }
                if (!filesToDelete.isEmpty()) {
                    deleteAdjuntos();
                }
                panel.refreshTable();
                JOptionPane.showMessageDialog(this, "La solicitud fue modificada con exito", "Solicitud modificada", 1);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "No se pudo editar la solicitud seleccionada", "Error al modificar", 0);
            }
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnAdjuntosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdjuntosActionPerformed
        handleChooser();
    }//GEN-LAST:event_btnAdjuntosActionPerformed

    private void btnEliminarAdjuntoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarAdjuntoActionPerformed
        try {
            removeAdjunto();
//            refreshAdjuntos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(rootPane, "Hubo un problema seleccionando adjuntos para eliminar!");
        }
    }//GEN-LAST:event_btnEliminarAdjuntoActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void listAdjuntosFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_listAdjuntosFocusGained
        if (!listModel.getElementAt(0).equals("No hay adjuntos")) {
            btnEliminarAdjunto.setEnabled(true);
        }
    }//GEN-LAST:event_listAdjuntosFocusGained

    private void btnEliminarAdjuntosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarAdjuntosActionPerformed
        removeAllAdjuntos();
    }//GEN-LAST:event_btnEliminarAdjuntosActionPerformed

    private void btnEliminarNewAdjuntosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarNewAdjuntosActionPerformed
        removeAllNewAdjuntos();
    }//GEN-LAST:event_btnEliminarNewAdjuntosActionPerformed

    private void btnEliminarNewAdjuntoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarNewAdjuntoActionPerformed
        removeSelectedNewAdjuntos();
    }//GEN-LAST:event_btnEliminarNewAdjuntoActionPerformed

    private void listNewAdjuntosFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_listNewAdjuntosFocusGained
        if (!listNewModel.getElementAt(0).equals("No hay nuevos adjuntos")) {
            btnEliminarNewAdjunto.setEnabled(true);
        }
    }//GEN-LAST:event_listNewAdjuntosFocusGained

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
    private javax.swing.JButton btnAdjuntos;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEliminarAdjunto;
    private javax.swing.JButton btnEliminarAdjuntos;
    private javax.swing.JButton btnEliminarNewAdjunto;
    private javax.swing.JButton btnEliminarNewAdjuntos;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<String> comboEvento;
    private javax.swing.JComboBox<String> comboTipoConstancia;
    private javax.swing.JLabel countChars;
    private javax.swing.JTextArea fieldDetalle;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JList<AdjuntoSolicitud> listAdjuntos;
    private javax.swing.JList<File> listNewAdjuntos;
    // End of variables declaration//GEN-END:variables
}
