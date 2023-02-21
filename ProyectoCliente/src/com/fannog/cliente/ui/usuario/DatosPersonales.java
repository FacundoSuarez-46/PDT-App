package com.fannog.cliente.ui.usuario;

import com.fannog.cliente.utils.Globals;
import com.fannog.servidor.entities.Estudiante;
import com.fannog.servidor.entities.Tutor;
import com.fannog.servidor.entities.Usuario;
import com.fannog.servidor.exceptions.ServicioException;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class DatosPersonales extends javax.swing.JPanel {

    private JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

    public DatosPersonales() {
        initComponents();

        showTutorFields(false);
        showEstudianteFields(false);

        try {
            loadFields();
        } catch (Exception ex) {
        }
    }

    private void showTutorFields(boolean show) {
        lblArea1.setVisible(show);
        lblRol1.setVisible(show);
        lblArea.setVisible(show);
        lblRol.setVisible(show);
    }

    private void showEstudianteFields(boolean show) {
        lblGeneracion.setVisible(show);
        lblGeneracion1.setVisible(show);
    }

    public void loadFields() throws ServicioException, Exception {
        Usuario usuario = Globals.getLoggedUser();

        lblNombres1.setText(usuario.getNombres());
        lblApellidos1.setText(usuario.getApellidos());
        lblNombreUsuario1.setText(usuario.getNombreUsuario());
        lblDocumento1.setText(usuario.getDocumento());
        lblEmail1.setText(usuario.getEmail());
        lblLocalida1.setText(usuario.getLocalidad().getNombre());
        lblDepartamento1.setText(usuario.getLocalidad().getDepartamento().getNombre());
        lblITR1.setText(usuario.getItr().getNombre());
        lblTelefono1.setText(String.valueOf(usuario.getTelefono()));

        if (usuario instanceof Estudiante estudiante) {
            showEstudianteFields(true);

            lblGeneracion1.setText(String.valueOf(estudiante.getGeneracion()));

            return;
        }

        if (usuario instanceof Tutor tutor) {
            showTutorFields(true);

            lblArea1.setText(tutor.getArea());
            lblRol1.setText(tutor.getRol());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblNombres = new javax.swing.JLabel();
        lblApellidos = new javax.swing.JLabel();
        lblNombreUsuario = new javax.swing.JLabel();
        lblPassword = new javax.swing.JLabel();
        lblDocumento = new javax.swing.JLabel();
        lblTelefono = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        lblITR = new javax.swing.JLabel();
        lblNombres1 = new javax.swing.JLabel();
        lblApellidos1 = new javax.swing.JLabel();
        lblNombreUsuario1 = new javax.swing.JLabel();
        lblDocumento1 = new javax.swing.JLabel();
        lblTelefono1 = new javax.swing.JLabel();
        lblEmail1 = new javax.swing.JLabel();
        lblITR1 = new javax.swing.JLabel();
        lblGeneracion = new javax.swing.JLabel();
        lblGeneracion1 = new javax.swing.JLabel();
        lblArea = new javax.swing.JLabel();
        lblArea1 = new javax.swing.JLabel();
        lblRol = new javax.swing.JLabel();
        lblRol1 = new javax.swing.JLabel();
        btnModificarDatos = new javax.swing.JButton();
        btnModificarPass = new javax.swing.JButton();
        lblDepartamento = new javax.swing.JLabel();
        lblLocalidad = new javax.swing.JLabel();
        lblDepartamento1 = new javax.swing.JLabel();
        lblLocalida1 = new javax.swing.JLabel();

        lblNombres.setText("Nombres:");

        lblApellidos.setText("Apellidos:");

        lblNombreUsuario.setText("Nombre de Usuario:");

        lblPassword.setText("Contraseña:");

        lblDocumento.setText("Documento:");

        lblTelefono.setText("Teléfono:");

        lblEmail.setText("Email:");

        lblITR.setText("ITR:");

        lblNombres1.setText("Nombre");

        lblApellidos1.setText("Apellido");

        lblNombreUsuario1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNombreUsuario1.setText("Usuario");

        lblDocumento1.setText("55555555");

        lblTelefono1.setText("09100000");

        lblEmail1.setText("@utec");

        lblITR1.setText("ITR");

        lblGeneracion.setText("Generación:");

        lblGeneracion1.setText("Gen");

        lblArea.setText("Área:");

        lblArea1.setText("Area");

        lblRol.setText("Rol:");

        lblRol1.setText("Rol");

        btnModificarDatos.setText("Modificar Datos");
        btnModificarDatos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnModificarDatosMouseClicked(evt);
            }
        });

        btnModificarPass.setText("Modificar");
        btnModificarPass.setPreferredSize(new java.awt.Dimension(81, 32));
        btnModificarPass.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnModificarPassMouseClicked(evt);
            }
        });

        lblDepartamento.setText("Departamento:");

        lblLocalidad.setText("Localidad:");

        lblDepartamento1.setText("Departamento");

        lblLocalida1.setText("Localidad");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblPassword)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 390, Short.MAX_VALUE)
                        .addComponent(btnModificarPass, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblNombres)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblNombres1))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblApellidos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblApellidos1))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblNombreUsuario)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblNombreUsuario1))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDocumento)
                            .addComponent(lblITR)
                            .addComponent(lblRol)
                            .addComponent(lblTelefono)
                            .addComponent(lblEmail)
                            .addComponent(lblDepartamento)
                            .addComponent(lblLocalidad)
                            .addComponent(lblArea)
                            .addComponent(lblGeneracion))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDocumento1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblEmail1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblTelefono1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblDepartamento1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblLocalida1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblITR1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblGeneracion1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblArea1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblRol1, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addGap(35, 35, 35))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnModificarDatos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNombres)
                    .addComponent(lblNombres1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblApellidos)
                    .addComponent(lblApellidos1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNombreUsuario)
                    .addComponent(lblNombreUsuario1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPassword)
                    .addComponent(btnModificarPass, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDocumento)
                    .addComponent(lblDocumento1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEmail)
                    .addComponent(lblEmail1))
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTelefono)
                    .addComponent(lblTelefono1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDepartamento)
                    .addComponent(lblDepartamento1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLocalidad)
                    .addComponent(lblLocalida1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblITR)
                    .addComponent(lblITR1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblGeneracion)
                    .addComponent(lblGeneracion1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblArea)
                    .addComponent(lblArea1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblRol)
                    .addComponent(lblRol1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 36, Short.MAX_VALUE)
                .addComponent(btnModificarDatos, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnModificarDatosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnModificarDatosMouseClicked
        new ModificarUsuario(parentFrame, false, Globals.getLoggedUser(), this, null).setVisible(true);
    }//GEN-LAST:event_btnModificarDatosMouseClicked

    private void btnModificarPassMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnModificarPassMouseClicked
        ModificarPassword modPass = new ModificarPassword();
        modPass.setVisible(true);
    }//GEN-LAST:event_btnModificarPassMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnModificarDatos;
    private javax.swing.JButton btnModificarPass;
    private javax.swing.JLabel lblApellidos;
    private javax.swing.JLabel lblApellidos1;
    private javax.swing.JLabel lblArea;
    private javax.swing.JLabel lblArea1;
    private javax.swing.JLabel lblDepartamento;
    private javax.swing.JLabel lblDepartamento1;
    private javax.swing.JLabel lblDocumento;
    private javax.swing.JLabel lblDocumento1;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblEmail1;
    private javax.swing.JLabel lblGeneracion;
    private javax.swing.JLabel lblGeneracion1;
    private javax.swing.JLabel lblITR;
    private javax.swing.JLabel lblITR1;
    private javax.swing.JLabel lblLocalida1;
    private javax.swing.JLabel lblLocalidad;
    private javax.swing.JLabel lblNombreUsuario;
    private javax.swing.JLabel lblNombreUsuario1;
    private javax.swing.JLabel lblNombres;
    private javax.swing.JLabel lblNombres1;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblRol;
    private javax.swing.JLabel lblRol1;
    private javax.swing.JLabel lblTelefono;
    private javax.swing.JLabel lblTelefono1;
    // End of variables declaration//GEN-END:variables
}
