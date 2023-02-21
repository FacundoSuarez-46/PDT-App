package com.fannog.cliente.ui.analista;

import com.fannog.cliente.sidebar.SidebarItemModel;
import com.fannog.cliente.ui.eventos.GestionEstadoEventos;
import com.fannog.cliente.ui.eventos.GestionModalidadEvento;
import com.fannog.cliente.ui.eventos.ListaConvocatorias;
import com.fannog.cliente.ui.eventos.ListaEventos;
import com.fannog.cliente.ui.login.Login;
import com.fannog.cliente.ui.reportes.ReportesReclamo;
import com.fannog.cliente.ui.usuario.DatosPersonales;
import com.fannog.cliente.utils.Globals;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MenuAnalista extends JFrame {

    public MenuAnalista() {
        initComponents();

        init();
    }

    private void init() {
        sidebar.addTitle("ADMINISTRACIÓN");
        sidebar.addMenuItem(new SidebarItemModel("Constancias", "Solicitudes", "Tipos"));
        sidebar.addMenuItem(new SidebarItemModel("Usuarios", "Listado", "ITRs"));
        sidebar.addMenuItem(new SidebarItemModel("Eventos", "Listado", "Convocatorias", "Estados", "Modalidades"));
        sidebar.addMenuItem(new SidebarItemModel("Reportes", "Reclamos"));
        sidebar.addTitle("CONFIGURACIÓN");
        sidebar.addMenuItem(new SidebarItemModel("Datos personales"));
        sidebar.addEmpty();
        sidebar.addMenuItem(new SidebarItemModel("Cerrar sesión"));

        sidebar.addEvent(this::handleSidebarSelection);

        sidebar.setSelectedIndex(0, 1);

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
                switch (indexSubMenu) {
                    case 1 -> {
                        showForm(new Solicitudes());
                    }
                    case 2 -> {
                        showForm(new TiposConstancia());
                    }
                }
            }
            case 1 -> {
                switch (indexSubMenu) {
                    case 1 -> {
                        showForm(new ListaUsuarios());
                    }
                    case 2 -> {
                        showForm(new GestionItrs());
                    }
                }
            }
            case 2 -> {
                switch (indexSubMenu) {
                    case 1 -> {
                        showForm(new ListaEventos());
                    }
                    case 2 -> {
                        showForm(new ListaConvocatorias());
                    }
                    case 3 -> {
                        showForm(new GestionEstadoEventos());
                    }
                    case 4 -> {
                        showForm(new GestionModalidadEvento());
                    }
                }
            }
            case 3 -> {
                showForm(new ReportesReclamo());
            }
            case 4 -> {
                showForm(new DatosPersonales());
            }
            case 5 -> {
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

        constancias1 = new com.fannog.cliente.ui.analista.Constancias();
        sidebar = new com.fannog.cliente.sidebar.Sidebar();
        jScrollPane1 = new javax.swing.JScrollPane();
        body = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Menu - Analista");
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        jScrollPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        body.setLayout(new java.awt.BorderLayout());
        jScrollPane1.setViewportView(body);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(sidebar, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 696, Short.MAX_VALUE)
                .addGap(12, 12, 12))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sidebar, javax.swing.GroupLayout.DEFAULT_SIZE, 644, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jScrollPane1)
                .addGap(12, 12, 12))
        );

        setSize(new java.awt.Dimension(1016, 652));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel body;
    private com.fannog.cliente.ui.analista.Constancias constancias1;
    private javax.swing.JScrollPane jScrollPane1;
    private com.fannog.cliente.sidebar.Sidebar sidebar;
    // End of variables declaration//GEN-END:variables
}
