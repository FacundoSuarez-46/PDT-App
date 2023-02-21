package com.fannog.cliente.ui.usuario;

import java.time.LocalDate;

public class EstudianteForm extends javax.swing.JPanel {

    public EstudianteForm() {
        initComponents();

        populateComboGeneracion();
    }

    public void setAñoIngreso(String year) {
        comboGeneracion.setSelectedItem(year);
    }

    public String getAñoIngreso() {
        return comboGeneracion.getSelectedItem().toString();
    }

    private void populateComboGeneracion() {

        LocalDate current_date = LocalDate.now();
        int current_Year = current_date.getYear();

        for (int i = 2015; i <= current_Year; i++) {
            String year = String.valueOf(i);
            comboGeneracion.addItem(year);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblAnioIngreso = new javax.swing.JLabel();
        comboGeneracion = new javax.swing.JComboBox<>();

        lblAnioIngreso.setFont(new java.awt.Font("Source Sans Pro", 0, 18)); // NOI18N
        lblAnioIngreso.setText("Año de ingreso");
        lblAnioIngreso.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        comboGeneracion.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        comboGeneracion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboGeneracionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(lblAnioIngreso)
                .addContainerGap(287, Short.MAX_VALUE))
            .addComponent(comboGeneracion, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(lblAnioIngreso)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(comboGeneracion, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void comboGeneracionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboGeneracionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboGeneracionActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> comboGeneracion;
    private javax.swing.JLabel lblAnioIngreso;
    // End of variables declaration//GEN-END:variables
}
