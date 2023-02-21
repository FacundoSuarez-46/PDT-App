package com.fannog.cliente.ui.eventos;

import com.fannog.cliente.utils.BeanFactory;
import com.fannog.cliente.utils.Globals;
import com.fannog.servidor.DAO.EstadoEventoDAO;
import com.fannog.servidor.DAO.EventoDAO;
import com.fannog.servidor.DAO.ItrDAO;
import com.fannog.servidor.DAO.ModalidadEventoDAO;
import com.fannog.servidor.DAO.TipoEventoDAO;
import com.fannog.servidor.DAO.TutorDAO;
import com.fannog.servidor.entities.EstadoEvento;
import com.fannog.servidor.entities.Evento;
import com.fannog.servidor.entities.Itr;
import com.fannog.servidor.entities.ModalidadEvento;
import com.fannog.servidor.entities.TipoEvento;
import com.fannog.servidor.entities.Tutor;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;

public class AltaOModEvento extends javax.swing.JDialog {

    private Evento evento;
    private ListaEventos listaEventos;

    private DefaultComboBoxModel comboTutoresModel;

    public AltaOModEvento(java.awt.Frame parent, boolean modal, Evento evento, ListaEventos listaEventos) {
        super(parent, modal);
        this.evento = evento;
        this.listaEventos = listaEventos;
        initComponents();

        myInit();
    }

    private void myInit() {
        clearList();

        try {
            populateEstados();
            populateComboITR();
            populateModalidad();
            populateTipoEvento();
            populateTutores();
        } catch (Exception ex) {
        }

        if (evento == null) {
            btnCrearOEditar.setText("Crear");

            this.setTitle("Fannog - Crear evento");
            this.evento = new Evento();

            comboEstado.getModel().setSelectedItem("ACTIVO");
        } else {
            btnCrearOEditar.setText("Actualizar");
            comboEstado.setEnabled(true);

            this.setTitle("Fannog - Actualizar evento");

            loadFields();
        }

        listTutores.getSelectionModel().addListSelectionListener(this::handleListTutoresSelection);

        if (comboTutor.getSelectedIndex() != -1) {
            btnAñadir.setEnabled(true);
        }
    }

    private void populateComboITR() throws Exception {
        ItrDAO itrDAO = BeanFactory.local().lookup("Itr");

        List<Itr> itrs = itrDAO.findAll();

        comboITR.setModel(new DefaultComboBoxModel(itrs.toArray()));
    }

    private void populateTipoEvento() throws Exception {
        TipoEventoDAO tipoEventoDAO = BeanFactory.local().lookup("TipoEvento");

        List<TipoEvento> tiposEventos = tipoEventoDAO.findAll();

        comboTipoEvento.setModel(new DefaultComboBoxModel(tiposEventos.toArray()));
    }

    private void populateModalidad() throws Exception {
        ModalidadEventoDAO modalidadEventoDAO = BeanFactory.local().lookup("ModalidadEvento");

        List<ModalidadEvento> modalidadesEventos = modalidadEventoDAO.findAll();

        comboModalidad.setModel(new DefaultComboBoxModel(modalidadesEventos.toArray()));
    }

    private void populateEstados() throws Exception {
        EstadoEventoDAO estadoEventoDAO = BeanFactory.local().lookup("EstadoEvento");

        List<EstadoEvento> estadosEventos = estadoEventoDAO.findAll();

        comboEstado.setModel(new DefaultComboBoxModel(estadosEventos.toArray()));
    }

    private void populateTutores() throws Exception {
        TutorDAO tutorDAO = BeanFactory.local().lookup("Tutor");

        List<Tutor> tutores = tutorDAO.findAll();

        comboTutoresModel = new DefaultComboBoxModel(tutores.toArray());

        comboTutor.setModel(comboTutoresModel);
    }

    public LocalDateTime convertDateToLocalDateTime(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public Date convertLocalDateTimeToDate(String stringToConver) {
        LocalDateTime dateToConvert = LocalDateTime.parse(stringToConver, Globals.dateTimeFormatter);

        return java.sql.Timestamp.valueOf(dateToConvert);
    }

    private DefaultListModel clearList() {
        DefaultListModel modelo = new DefaultListModel();
        listTutores.setModel(modelo);

        return modelo;
    }

    public DefaultListModel agregarTutor() {
        Tutor t = (Tutor) comboTutor.getSelectedItem();
        DefaultListModel modelo = (DefaultListModel) listTutores.getModel();

        modelo.addElement(t);

        comboTutor.removeItem(t);

        return modelo;
    }

    public DefaultListModel removerTutor() {
        Tutor t = listTutores.getSelectedValue();

        DefaultListModel modelo = (DefaultListModel) listTutores.getModel();
        modelo.removeElement(t);

        comboTutoresModel.addElement(t);

        return modelo;
    }

    private void createOrUpdateEvento() throws Exception {
        EventoDAO eventoDAO = BeanFactory.local().lookup("Evento");

        String titulo = txtTitulo.getText();
        LocalDateTime fechaFinal = convertDateToLocalDateTime(dateFin.getDate());
        LocalDateTime fechaInicio = convertDateToLocalDateTime(dateIncio.getDate());

        Itr itr = (Itr) comboITR.getSelectedItem();
        ModalidadEvento modalidadEvento = (ModalidadEvento) comboModalidad.getSelectedItem();
        TipoEvento tipoEvento = (TipoEvento) comboTipoEvento.getSelectedItem();
        String localizacion = txtLocalizacion.getText();

        this.evento.setNombre(titulo);
        this.evento.setFecHoraInicio(fechaInicio);
        this.evento.setFecHoraFinal(fechaFinal);
        this.evento.setItr(itr);
        this.evento.setModalidad(modalidadEvento);
        this.evento.setTipo(tipoEvento);
        this.evento.setLocalizacion(localizacion);

        EstadoEvento estadoEvento = null;

        if (this.evento.getId() == null) {
            EstadoEventoDAO estadoEventoDAO = BeanFactory.local().lookup("EstadoEvento");
            estadoEvento = estadoEventoDAO.findById(1L);
        } else {
            estadoEvento = (EstadoEvento) comboEstado.getSelectedItem();
        }

        this.evento.setEstado(estadoEvento);

        DefaultListModel listModel = (DefaultListModel) listTutores.getModel();

        List<Tutor> tutores = new ArrayList<>();

        for (int i = 0; i < listModel.getSize(); i++) {
            Tutor tutor = (Tutor) listModel.getElementAt(i);
            tutores.add(tutor);
        }

        if (this.evento.getId() == null) {
            eventoDAO.create(this.evento, tutores);
        } else {
            eventoDAO.edit(this.evento, tutores);
        }

        listaEventos.refreshTable();

    }

    private void loadFields() {
        txtTitulo.setText(evento.getNombre());
        dateIncio.setDate(convertLocalDateTimeToDate(evento.getFecHoraInicio()));
        dateFin.setDate(convertLocalDateTimeToDate(evento.getFecHoraFinal()));
        comboITR.getModel().setSelectedItem(evento.getItr());
        comboModalidad.getModel().setSelectedItem(evento.getModalidad());
        comboTipoEvento.getModel().setSelectedItem(evento.getTipo());
        comboEstado.getModel().setSelectedItem(evento.getEstado());
        txtLocalizacion.setText(evento.getLocalizacion());

        List<Tutor> tutores = new LinkedList<>();
        tutores.addAll(evento.getTutores());
        DefaultListModel listModel = (DefaultListModel) listTutores.getModel();

        listModel.addAll(tutores);
        listTutores.setModel(listModel);
    }

    public boolean validateData() {
        String titulo = txtTitulo.getText().trim();
        LocalDateTime fechaI = convertDateToLocalDateTime(dateIncio.getDate());
        LocalDateTime fechaF = convertDateToLocalDateTime(dateFin.getDate());
        LocalDateTime ahora = LocalDateTime.now();
        String localizacion = txtLocalizacion.getText().trim();

        if (titulo.length() < 2 || titulo.length() > 50) {
            JOptionPane.showMessageDialog(null, "El campo titulo debe tener entre 2 y 50 caracteres", "Campo Titulo Incorrecto", JOptionPane.ERROR_MESSAGE);

            return false;
        }

        if (ahora.isAfter(fechaI)) {
            JOptionPane.showMessageDialog(null, "La fecha inicial no puede ser anterior a la fecha actual", "Campo Fecha Incorrecto", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (fechaI.isAfter(fechaF)) {
            JOptionPane.showMessageDialog(null, "La fecha final no puede ser anterior a la fecha inicio", "Campo Fecha Incorrecto", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (localizacion.length() > 50) {
            JOptionPane.showMessageDialog(null, "El campo localización no puede tener mas de 50 caracteres", "Campo Localización Incorrecto", JOptionPane.ERROR_MESSAGE);

            return false;
        }

        return true;

    }

    public void handleListTutoresSelection(ListSelectionEvent e) {
        boolean seleccionValida = !listTutores.isSelectionEmpty();

        btnQuitar.setEnabled(seleccionValida);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        comboTutor = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        comboModalidad = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        btnCrearOEditar = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        comboITR = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        dateIncio = new com.toedter.calendar.JDateChooser();
        txtLocalizacion = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        btnQuitar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        dateFin = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        comboTipoEvento = new javax.swing.JComboBox<>();
        txtTitulo = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        listTutores = new javax.swing.JList<>();
        btnAñadir = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        comboEstado = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        comboTutor.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        comboTutor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboTutorItemStateChanged(evt);
            }
        });
        comboTutor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboTutorActionPerformed(evt);
            }
        });
        comboTutor.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Tutor) {
                    Tutor tutor = (Tutor) value;
                    setText(tutor.getNombres() + " " + tutor.getApellidos());
                }
                return this;
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel7.setText("ITR");

        comboModalidad.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        comboModalidad.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof ModalidadEvento) {
                    ModalidadEvento modalidadEvento = (ModalidadEvento) value;
                    setText(modalidadEvento.getNombre());
                }
                return this;
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setText("Fecha y Hora de Finalizado");

        btnCrearOEditar.setText("Crear");
        btnCrearOEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCrearOEditarActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel9.setText("Tutor/es encargado/s");

        comboITR.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        comboITR.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Itr) {
                    Itr itr = (Itr) value;
                    setText(itr.getNombre());
                }
                return this;
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("Tipo de Evento");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel8.setText("Localización");

        dateIncio.setDateFormatString("dd/MM/yyyy HH:mm");
        dateIncio.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        txtLocalizacion.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setText("Modalidad");

        btnQuitar.setText("Quitar");
        btnQuitar.setEnabled(false);
        btnQuitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitarActionPerformed(evt);
            }
        });

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        dateFin.setDateFormatString("dd/MM/yyyy HH:mm");
        dateFin.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("Título del Evento");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel10.setText("Fecha y Hora de Inicio");

        comboTipoEvento.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        comboTipoEvento.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof TipoEvento) {
                    TipoEvento tipoEvento = (TipoEvento) value;
                    setText(tipoEvento.getNombre());
                }
                return this;
            }
        });

        txtTitulo.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        listTutores.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        listTutores.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                listTutoresFocusGained(evt);
            }
        });
        listTutores.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value instanceof Tutor) {
                    Tutor t = (Tutor) value;
                    setText(t.getNombres() + " " +t.getApellidos());
                }
                return this;
            }
        }
    );
    jScrollPane1.setViewportView(listTutores);

    btnAñadir.setText("Añadir");
    btnAñadir.setEnabled(false);
    btnAñadir.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnAñadirActionPerformed(evt);
        }
    });

    jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
    jLabel11.setText("Estado");

    comboEstado.setEnabled(false);
    comboEstado.setRenderer(new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof EstadoEvento) {
                EstadoEvento estadoEvento = (EstadoEvento) value;
                setText(estadoEvento.getNombre());
            }
            return this;
        }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(btnCancelar)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(btnCrearOEditar))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(btnQuitar)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(btnAñadir))))
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
                        .addComponent(txtTitulo, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(comboTipoEvento, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(comboModalidad, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(comboITR, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtLocalizacion)
                        .addComponent(comboTutor, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(dateIncio, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(dateFin, javax.swing.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
                        .addComponent(comboEstado, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGap(14, 14, 14))
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addGap(12, 12, 12)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(txtTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(comboTipoEvento, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(dateIncio, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(9, 9, 9)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(18, 18, 18)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(21, 21, 21))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addComponent(dateFin, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(comboModalidad, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(comboITR, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(txtLocalizacion, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel8))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(comboEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(comboTutor, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnAñadir)
                .addComponent(btnQuitar))
            .addGap(48, 48, 48)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnCancelar)
                .addComponent(btnCrearOEditar))
            .addGap(12, 12, 12))
    );

    pack();
    setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void comboTutorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboTutorActionPerformed

    }//GEN-LAST:event_comboTutorActionPerformed

    private void btnCrearOEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCrearOEditarActionPerformed
        if (validateData()) {
            try {
                createOrUpdateEvento();
                JOptionPane.showMessageDialog(this, "Evento guardado correctamente");

                dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnCrearOEditarActionPerformed

    private void btnQuitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitarActionPerformed
        removerTutor();
    }//GEN-LAST:event_btnQuitarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed

    }//GEN-LAST:event_btnCancelarActionPerformed

    private void listTutoresFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_listTutoresFocusGained
    }//GEN-LAST:event_listTutoresFocusGained

    private void btnAñadirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAñadirActionPerformed
        agregarTutor();
    }//GEN-LAST:event_btnAñadirActionPerformed

    private void comboTutorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboTutorItemStateChanged
        boolean seleccionValida = ItemEvent.SELECTED == evt.getStateChange();

        btnAñadir.setEnabled(seleccionValida);
    }//GEN-LAST:event_comboTutorItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAñadir;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnCrearOEditar;
    private javax.swing.JButton btnQuitar;
    private javax.swing.JComboBox<String> comboEstado;
    private javax.swing.JComboBox<String> comboITR;
    private javax.swing.JComboBox<String> comboModalidad;
    private javax.swing.JComboBox<String> comboTipoEvento;
    private javax.swing.JComboBox<String> comboTutor;
    private com.toedter.calendar.JDateChooser dateFin;
    private com.toedter.calendar.JDateChooser dateIncio;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList<Tutor> listTutores;
    private javax.swing.JTextField txtLocalizacion;
    private javax.swing.JTextField txtTitulo;
    // End of variables declaration//GEN-END:variables
}
