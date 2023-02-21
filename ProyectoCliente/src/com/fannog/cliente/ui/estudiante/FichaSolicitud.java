package com.fannog.cliente.ui.estudiante;

import com.fannog.cliente.utils.BeanFactory;
import com.fannog.cliente.utils.Globals;
import com.fannog.servidor.DAO.AdjuntoSolicitudDAO;
import com.fannog.servidor.entities.AdjuntoSolicitud;
import com.fannog.servidor.entities.Solicitud;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;

public class FichaSolicitud extends javax.swing.JDialog {

    ListadoSolicitudes panel;
    Solicitud solicitud;
    List<AdjuntoSolicitud> adjuntos;

    DefaultListModel listModel = new DefaultListModel();

    public FichaSolicitud(java.awt.Frame parent, boolean modal, ListadoSolicitudes panel) {
        super(parent, modal);
        this.panel = panel;
        initComponents();
        myComponentsInit();
    }

    private void myComponentsInit() {
        try {
            AdjuntoSolicitudDAO adjuntoSolicitudDAO = BeanFactory.local().lookup("AdjuntoSolicitud");

            cleanList();
            setSolicitud(panel.getSelectedSolicitud());
            setAdjuntos(adjuntoSolicitudDAO.findAllBySolicitud(solicitud.getId()));
            loadData();
            loadAdjuntos();
        } catch (Exception ex) {
            Logger.getLogger(FichaSolicitud.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadData() {

        nombreValue.setText(solicitud.getEstudiante().getNombres() + solicitud.getEstudiante().getApellidos());
        idValue.setText((solicitud.getId().toString()));
        detalleValue.setText(solicitud.getDetalle());
        fechaValue.setText(((solicitud.getFecha()).format(Globals.dateTimeFormatter)).toString());
        estadoValue.setText(solicitud.getEstado().getNombre());
        eventoValue.setText(solicitud.getEvento().getNombre());
        tipoConstValue.setText(solicitud.getTipo().getNombre());
    }

    public DefaultListModel cleanList() {
        listModel = new DefaultListModel();
        listAdjuntos.setModel(listModel);

        return listModel;
    }

    public DefaultListModel loadAdjuntos() {
        try {
            listModel = (DefaultListModel) listAdjuntos.getModel();

            for (AdjuntoSolicitud a : getAdjuntos()) {
                if (!a.isEliminado()) {
                    listModel.addElement(a);
                }
            }

            if (listModel.isEmpty()) {
                listModel.addElement("No hay adjuntos");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return listModel;
    }

// Getters Setters Getters Setters
    public Solicitud getSolicitud() {
        return solicitud;
    }

    private void setSolicitud(Solicitud solicitud) {
        this.solicitud = solicitud;
    }

    public List<AdjuntoSolicitud> getAdjuntos() {
        return adjuntos;
    }

    private void setAdjuntos(List<AdjuntoSolicitud> lista) {
        this.adjuntos = lista;
    }

// Getters Setters Getters Setters
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        fechaValue = new javax.swing.JLabel();
        estadoValue = new javax.swing.JLabel();
        eventoValue = new javax.swing.JLabel();
        tipoConstValue = new javax.swing.JLabel();
        nombreValue = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listAdjuntos = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        detalleValue = new javax.swing.JTextArea();
        idValue = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(654, 669));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("ID:");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("Detalle:");

        fechaValue.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        fechaValue.setText("Fecha");

        estadoValue.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        estadoValue.setText("Estado");

        eventoValue.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        eventoValue.setText("Evento");

        tipoConstValue.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        tipoConstValue.setText("Tipo Constancia");

        nombreValue.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        nombreValue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        nombreValue.setText("Nombre");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel8.setText("Adjuntos:");

        listAdjuntos.setEnabled(false);
        listAdjuntos.setFocusable(false);
        listAdjuntos.setRequestFocusEnabled(false);
        listAdjuntos.setVerifyInputWhenFocusTarget(false);
        jScrollPane1.setViewportView(listAdjuntos);

        detalleValue.setEditable(false);
        detalleValue.setColumns(20);
        detalleValue.setRows(5);
        detalleValue.setRequestFocusEnabled(false);
        jScrollPane2.setViewportView(detalleValue);

        idValue.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        idValue.setText("ID");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel10.setText("Fecha:");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel11.setText("Estado:");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel12.setText("Evento:");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel13.setText("Tipo Constancia:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(idValue)
                                .addGap(44, 44, 44))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel8))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 135, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
                                    .addComponent(jScrollPane2))))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(fechaValue))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(tipoConstValue))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(estadoValue))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(eventoValue)))
                        .addGap(37, 37, 37))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(nombreValue)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(nombreValue)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(idValue))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(jLabel2))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fechaValue)
                    .addComponent(jLabel10))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(estadoValue)
                    .addComponent(jLabel11))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(eventoValue)
                    .addComponent(jLabel12))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tipoConstValue)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea detalleValue;
    private javax.swing.JLabel estadoValue;
    private javax.swing.JLabel eventoValue;
    private javax.swing.JLabel fechaValue;
    private javax.swing.JLabel idValue;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList<String> listAdjuntos;
    private javax.swing.JLabel nombreValue;
    private javax.swing.JLabel tipoConstValue;
    // End of variables declaration//GEN-END:variables
}
