package com.fannog.cliente.ui.reportes;

import com.fannog.cliente.utils.BeanFactory;
import com.fannog.servidor.DAO.ReclamoDAO;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import java.util.Map;
import net.miginfocom.swing.MigLayout;

public class ReportesReclamo extends javax.swing.JPanel {

    private Gson gson = new Gson();
    JsonParser parser = new JsonParser();

    public ReportesReclamo() {
        initComponents();

        panel.setLayout(new MigLayout("wrap, insets 0, fillx", "[]12[]12[]", "[]12[]12[]"));

        filter();
    }

    private void filterByITR() throws Exception {
        ReclamoDAO reclamoDAO = BeanFactory.local().lookup("Reclamo");

        Map<String, String> result = reclamoDAO.countByItr();

        renderCard(result, "ITR");
    }

    private void filterByGeneracion() throws Exception {
        ReclamoDAO reclamoDAO = BeanFactory.local().lookup("Reclamo");

        Map<String, String> result = reclamoDAO.countByGeneracion();

        renderCard(result, "Generación");
    }

    private void renderCard(Map<String, String> map, String keyword) {
        map.entrySet().stream().forEach(entry -> {
            String title = keyword + " " + entry.getKey();
            String value = entry.getValue();

            Card card = new Card(title, value);

            panel.add(card, "growx");
        });
    }

    private void filter() {
        panel.removeAll();

        String item = comboFiltro.getSelectedItem().toString();

        switch (item) {
            case "POR ITR" -> {
                try {
                    filterByITR();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
            }

            case "POR GENERACION" -> {
                try {
                    filterByGeneracion();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
            }
        }

        panel.repaint();
        panel.revalidate();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        comboFiltro = new javax.swing.JComboBox<>();
        panel = new javax.swing.JPanel();

        comboFiltro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "POR GENERACION", "POR ITR", "POR MES" }));
        comboFiltro.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboFiltroItemStateChanged(evt);
            }
        });

        panel.setLayout(null);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(comboFiltro, 0, 640, Short.MAX_VALUE)
            .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(comboFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void comboFiltroItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboFiltroItemStateChanged
        filter();
    }//GEN-LAST:event_comboFiltroItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> comboFiltro;
    private javax.swing.JPanel panel;
    // End of variables declaration//GEN-END:variables
}
