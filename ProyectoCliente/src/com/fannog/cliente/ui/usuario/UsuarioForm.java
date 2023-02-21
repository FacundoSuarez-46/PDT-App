package com.fannog.cliente.ui.usuario;

import com.fannog.cliente.utils.BeanFactory;
import com.fannog.cliente.utils.FieldUtils;
import com.fannog.cliente.utils.Validator;
import com.fannog.servidor.DAO.DepartamentoDAO;
import com.fannog.servidor.DAO.EstadoUsuarioDAO;
import com.fannog.servidor.DAO.ItrDAO;
import com.fannog.servidor.entities.Analista;
import com.fannog.servidor.entities.Departamento;
import com.fannog.servidor.entities.EstadoUsuario;
import com.fannog.servidor.entities.Estudiante;
import com.fannog.servidor.entities.Itr;
import com.fannog.servidor.entities.Localidad;
import com.fannog.servidor.entities.Tutor;
import com.fannog.servidor.entities.Usuario;
import com.fannog.servidor.exceptions.ServicioException;
import java.awt.Component;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class UsuarioForm extends javax.swing.JPanel {

    private Usuario usuario;
    private final Validator validator = new Validator();
    DefaultComboBoxModel comboDepartamentoModel;
    DefaultComboBoxModel comboLocalidadModel;
    DefaultComboBoxModel comboItrModel;
    private boolean esTutor;
    private boolean esEstudiante;

    public UsuarioForm() {
        initComponents();

        showTipoUsuarioFields();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void loadData() throws Exception {
        populateComboDepartamento();
        populateComboITR();

        if (usuario == null) {
            comboTipoUsuario.setSelectedIndex(0);

            return;
        }

        txtNombres.setText(usuario.getNombres());
        txtApellidos.setText(usuario.getApellidos());
        txtDocumento.setText(usuario.getDocumento());
        txtEmail.setText(usuario.getEmail());
        txtTelefono.setText(String.valueOf(usuario.getTelefono()));
        comboITR.setSelectedItem(usuario.getItr());

        if (usuario instanceof Estudiante estudiante) {
            estudianteForm.setAñoIngreso(String.valueOf(estudiante.getGeneracion()));
        }

        if (usuario instanceof Tutor tutor) {
            tutorForm.setArea(tutor.getArea());
            tutorForm.setRol(tutor.getRol());
        }

        lblConstrasenia.setVisible(false);
        txtPassword.setVisible(false);
        txtEmail.setEnabled(false);
        comboTipoUsuario.setEnabled(false);

        comboDepartamentoModel.setSelectedItem(usuario.getLocalidad().getDepartamento());

        departamentoChanged();

        comboLocalidadModel.setSelectedItem(usuario.getLocalidad());

        comboItrModel.setSelectedItem(usuario.getItr());

        comboTipoUsuario.setSelectedItem(usuario.getClass().getSimpleName().toUpperCase());
    }

    private void showTipoUsuarioFields() {
        String selectedTipoUsuario = String.valueOf(comboTipoUsuario.getSelectedItem());

        this.esTutor = selectedTipoUsuario.equalsIgnoreCase("tutor");
        this.esEstudiante = selectedTipoUsuario.equalsIgnoreCase("estudiante");

        estudianteForm.setVisible(esEstudiante);
        tutorForm.setVisible(esTutor);
    }

    private void populateComboDepartamento() throws Exception {
        DepartamentoDAO depDAO = BeanFactory.local().lookup("Departamento");

        List<Departamento> departamentos = depDAO.findAllWithLocalidades();

        comboDepartamentoModel = new DefaultComboBoxModel(departamentos.toArray());

        comboDepartamento.setModel(comboDepartamentoModel);

        departamentoChanged();
    }

    private void departamentoChanged() {
        Departamento selectedDepartamento = (Departamento) comboDepartamentoModel.getSelectedItem();
        List<Localidad> localidades = selectedDepartamento.getLocalidades();

        comboLocalidadModel = new DefaultComboBoxModel(localidades.toArray());

        comboLocalidad.setModel(comboLocalidadModel);
    }

    private void populateComboITR() throws Exception {
        ItrDAO itrDAO = BeanFactory.local().lookup("Itr");

        List<Itr> itrs = itrDAO.findAll();

        comboItrModel = new DefaultComboBoxModel(itrs.toArray());

        comboITR.setModel(comboItrModel);
    }

    public void saveData() throws ServicioException, Exception {
        String nombres = txtNombres.getText();
        String apellidos = txtApellidos.getText();
        String documento = txtDocumento.getText();
        documento = validator.cleanCi(documento);
        String email = txtEmail.getText();
        Integer telefono = Integer.parseInt(txtTelefono.getText().replace(" ", ""));
        String password = new String(txtPassword.getPassword());
        Itr itr = (Itr) comboITR.getSelectedItem();
        Localidad localidad = (Localidad) comboLocalidad.getSelectedItem();

        validateFormData(password, email, telefono);

        handleUserType();

        usuario.setNombres(nombres);
        usuario.setApellidos(apellidos);
        usuario.setDocumento(documento);
        usuario.setEmail(email);
        usuario.setTelefono(telefono);
        usuario.setItr(itr);
        usuario.setLocalidad(localidad);

        if (usuario.getId() == null) {
            usuario.setPassword(password);

            EstadoUsuarioDAO estUsuDAO = BeanFactory.local().lookup("EstadoUsuario");
            EstadoUsuario estado = estUsuDAO.findById(1L);

            usuario.setEstado(estado);
        }
    }

    private void validateFormData(String password, String email, Integer telefono) throws ServicioException {
        int length = String.valueOf(telefono).length();

        if (length != 8) {
            throw new ServicioException("El teléfono debe tener formato 09XXXXXXX");
        }

        if (usuario != null) {
            return;
        }

        if (password.isBlank() || password.length() < 4 || password.length() > 20) {
            throw new ServicioException("La constaseña debe tener entre 4 y 20 caracteres");
        }

        if (!esEstudiante) {
            if (!email.endsWith("@utec.edu.uy")) {
                throw new ServicioException("El email debe tener formato @utec.edu.uy");
            }
        }

        if (esEstudiante) {
            if (!email.endsWith("@estudiantes.utec.edu.uy")) {
                throw new ServicioException("El email debe tener formato @estudiantes.utec.edu.uy");
            }
        }
    }

    private void handleUserType() {
        if (esTutor) {
            if (usuario == null) {
                usuario = new Tutor();
            }

            String area = tutorForm.getArea();
            String rol = tutorForm.getRol();

            ((Tutor) usuario).setArea(area);
            ((Tutor) usuario).setRol(rol);

            return;
        }

        if (esEstudiante) {
            if (usuario == null) {
                usuario = new Estudiante();
            }

            Integer generacion = Integer.parseInt(estudianteForm.getAñoIngreso());

            ((Estudiante) usuario).setGeneracion(generacion);

            return;
        }

        usuario = new Analista();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblNombres = new javax.swing.JLabel();
        txtNombres = new javax.swing.JTextField();
        lblApellidos = new javax.swing.JLabel();
        txtApellidos = new javax.swing.JTextField();
        lblDocumento = new javax.swing.JLabel();
        txtDocumento = new javax.swing.JFormattedTextField();
        lblEmail = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        lblTelefono = new javax.swing.JLabel();
        txtTelefono = new javax.swing.JFormattedTextField();
        lblConstrasenia = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        lblTipoDeUsuario = new javax.swing.JLabel();
        comboTipoUsuario = new javax.swing.JComboBox<>();
        lblLocalidad = new javax.swing.JLabel();
        comboLocalidad = new javax.swing.JComboBox<>();
        lblDepartamento = new javax.swing.JLabel();
        comboDepartamento = new javax.swing.JComboBox<>();
        lblITR = new javax.swing.JLabel();
        comboITR = new javax.swing.JComboBox<>();
        estudianteForm = new com.fannog.cliente.ui.usuario.EstudianteForm();
        tutorForm = new com.fannog.cliente.ui.usuario.TutorForm();

        lblNombres.setFont(new java.awt.Font("Source Sans Pro", 0, 18)); // NOI18N
        lblNombres.setText("Nombres");

        txtNombres.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtNombres.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombresKeyTyped(evt);
            }
        });

        lblApellidos.setFont(new java.awt.Font("Source Sans Pro", 0, 18)); // NOI18N
        lblApellidos.setText("Apellidos");

        txtApellidos.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtApellidos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtApellidosKeyTyped(evt);
            }
        });

        lblDocumento.setFont(new java.awt.Font("Source Sans Pro", 0, 18)); // NOI18N
        lblDocumento.setText("Documento");

        try {
            txtDocumento.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#.###.###-#")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtDocumento.setFont(new java.awt.Font("Source Sans Pro", 0, 18)); // NOI18N

        lblEmail.setFont(new java.awt.Font("Source Sans Pro", 0, 18)); // NOI18N
        lblEmail.setText("Email");

        txtEmail.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        lblTelefono.setFont(new java.awt.Font("Source Sans Pro", 0, 18)); // NOI18N
        lblTelefono.setText("Telefono");

        try {
            txtTelefono.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("09# ### ###")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtTelefono.setFont(new java.awt.Font("Source Sans Pro", 0, 18)); // NOI18N

        lblConstrasenia.setFont(new java.awt.Font("Source Sans Pro", 0, 18)); // NOI18N
        lblConstrasenia.setText("Contraseña");

        txtPassword.setFont(new java.awt.Font("Source Sans Pro", 0, 18)); // NOI18N

        lblTipoDeUsuario.setFont(new java.awt.Font("Source Sans Pro", 0, 18)); // NOI18N
        lblTipoDeUsuario.setText("Tipo de usuario");

        comboTipoUsuario.setFont(new java.awt.Font("Source Sans Pro", 0, 18)); // NOI18N
        comboTipoUsuario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ESTUDIANTE", "TUTOR", "ANALISTA" }));
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

        lblLocalidad.setFont(new java.awt.Font("Source Sans Pro", 0, 18)); // NOI18N
        lblLocalidad.setText("Localidad");

        comboLocalidad.setFont(new java.awt.Font("Source Sans Pro", 0, 18)); // NOI18N
        comboLocalidad.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Localidad) {
                    Localidad localidad = (Localidad) value;
                    setText(localidad.getNombre());
                }
                return this;
            }
        });

        lblDepartamento.setFont(new java.awt.Font("Source Sans Pro", 0, 18)); // NOI18N
        lblDepartamento.setText("Departamento");

        comboDepartamento.setFont(new java.awt.Font("Source Sans Pro", 0, 18)); // NOI18N
        comboDepartamento.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Departamento) {
                    Departamento departamento = (Departamento) value;
                    setText(departamento.getNombre());
                }
                return this;
            }
        });
        comboDepartamento.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboDepartamentoItemStateChanged(evt);
            }
        });

        lblITR.setFont(new java.awt.Font("Source Sans Pro", 0, 18)); // NOI18N
        lblITR.setText("ITR");

        comboITR.setFont(new java.awt.Font("Source Sans Pro", 0, 18)); // NOI18N
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tutorForm, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblLocalidad, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(156, 156, 156))
                    .addComponent(comboTipoUsuario, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTelefono)
                    .addComponent(txtDocumento)
                    .addComponent(comboLocalidad, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblTelefono)
                    .addComponent(lblDocumento)
                    .addComponent(lblTipoDeUsuario)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblNombres, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                        .addGap(219, 219, 219))
                    .addComponent(txtNombres))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtEmail)
                    .addComponent(txtPassword)
                    .addComponent(comboITR, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(comboDepartamento, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtApellidos, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDepartamento)
                            .addComponent(lblEmail)
                            .addComponent(lblConstrasenia)
                            .addComponent(lblITR)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblApellidos, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                                .addGap(39, 39, 39)))
                        .addGap(180, 180, 180))))
            .addComponent(estudianteForm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblNombres)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtNombres, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblApellidos)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblEmail)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblDocumento)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblTelefono)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtTelefono))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblConstrasenia)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblTipoDeUsuario)
                                .addGap(12, 12, 12)
                                .addComponent(comboTipoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblITR)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(comboITR, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblDepartamento)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(comboDepartamento, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblLocalidad)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(comboLocalidad, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(estudianteForm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tutorForm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtNombresKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombresKeyTyped
        FieldUtils.esTexto(evt);
    }//GEN-LAST:event_txtNombresKeyTyped

    private void txtApellidosKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtApellidosKeyTyped
        FieldUtils.esTexto(evt);
    }//GEN-LAST:event_txtApellidosKeyTyped

    private void comboTipoUsuarioItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboTipoUsuarioItemStateChanged
        showTipoUsuarioFields();
    }//GEN-LAST:event_comboTipoUsuarioItemStateChanged

    private void comboTipoUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboTipoUsuarioActionPerformed

    }//GEN-LAST:event_comboTipoUsuarioActionPerformed

    private void comboDepartamentoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboDepartamentoItemStateChanged
        departamentoChanged();
    }//GEN-LAST:event_comboDepartamentoItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> comboDepartamento;
    private javax.swing.JComboBox<String> comboITR;
    private javax.swing.JComboBox<String> comboLocalidad;
    private javax.swing.JComboBox<String> comboTipoUsuario;
    private com.fannog.cliente.ui.usuario.EstudianteForm estudianteForm;
    private javax.swing.JLabel lblApellidos;
    private javax.swing.JLabel lblConstrasenia;
    private javax.swing.JLabel lblDepartamento;
    private javax.swing.JLabel lblDocumento;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblITR;
    private javax.swing.JLabel lblLocalidad;
    private javax.swing.JLabel lblNombres;
    private javax.swing.JLabel lblTelefono;
    private javax.swing.JLabel lblTipoDeUsuario;
    private com.fannog.cliente.ui.usuario.TutorForm tutorForm;
    private javax.swing.JTextField txtApellidos;
    private javax.swing.JFormattedTextField txtDocumento;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtNombres;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JFormattedTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
