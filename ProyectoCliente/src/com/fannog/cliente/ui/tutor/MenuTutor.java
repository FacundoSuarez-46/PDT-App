package com.fannog.cliente.ui.tutor;

import com.fannog.cliente.sidebar.SidebarItemModel;
import com.fannog.cliente.ui.login.Login;
import com.fannog.cliente.ui.usuario.DatosPersonales;
import com.fannog.cliente.utils.Globals;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MenuTutor extends javax.swing.JFrame {

    public MenuTutor() {
        initComponents();

        init();
    }

    private void init() {
        sidebar.addTitle("CONFIGURACIÓN");
        sidebar.addMenuItem(new SidebarItemModel("Datos personales"));
        sidebar.addEmpty();
        sidebar.addMenuItem(new SidebarItemModel("Cerrar sesión"));

        sidebar.addEvent(this::handleSidebarSelection);

        sidebar.setSelectedIndex(0, 0);

        jScrollPane1.setBorder(BorderFactory.createEmptyBorder());
    }

    private void handleLogout() {
        int confirmarcion = JOptionPane.showConfirmDialog(null, "¿Quieres cerrar la sesión?", "Cerrar sesión", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirmarcion != JOptionPane.YES_OPTION) {
            return;
        }

        dispose();
        Globals.setLoggedUser(null);
        new Login().setVisible(true);
    }

    private void handleSidebarSelection(int index, int indexSubMenu) {
        switch (index) {
            case 0 -> {
                showForm(new DatosPersonales());
            }
            case 1 -> {
                handleLogout();
            }
        }
    }

    public void showForm(Component com) {
        body.removeAll();
        body.add(com);
        body.repaint();
        body.revalidate();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sidebar = new com.fannog.cliente.sidebar.Sidebar();
        jScrollPane1 = new javax.swing.JScrollPane();
        body = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Fannog - Menu Tutor");
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        body.setLayout(new java.awt.BorderLayout());
        jScrollPane1.setViewportView(body);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(sidebar, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 847, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sidebar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 736, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(1196, 744));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel body;
    private javax.swing.JScrollPane jScrollPane1;
    private com.fannog.cliente.sidebar.Sidebar sidebar;
    // End of variables declaration//GEN-END:variables
}
