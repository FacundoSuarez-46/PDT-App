package com.fannog.cliente.sidebar;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;

public class Sidebar extends javax.swing.JPanel {

    private int index = -1;
    private final List<EventSidebarSelected> events = new ArrayList<>();

    public Sidebar() {
        init();
    }

    private void init() {
        setBackground(UIManager.getColor("List.background"));

        setLayout(new BorderLayout());
        JScrollPane scroll = createScroll();
        panelMenu = createPanelMenu();
        scroll.setViewportView(panelMenu);
        scroll.getViewport().setOpaque(false);
        scroll.setViewportBorder(null);

        add(scroll);
    }

    private JScrollPane createScroll() {
        JScrollPane scroll = new JScrollPane();

        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBar(new JScrollBar());

        return scroll;
    }

    private JPanel createPanelMenu() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        menuLayout = new MigLayout("wrap,fillx,inset 0,gapy 0", "[fill]");
        panel.setLayout(menuLayout);

        return panel;
    }

    private JPanel createMenuItem(SidebarItemModel item) {
        SidebarItem menuItem = new SidebarItem(item, ++index, menuLayout);

        menuItem.addEvent((int index1, int indexSubMenu) -> {
            if (!menuItem.isHasSubMenu() || indexSubMenu != 0) {
                clearSelected();
                setSelectedIndex(index1, indexSubMenu);
            }
        });

        return menuItem;
    }

    private void runEvent(int index, int indexSubMenu) {
        for (EventSidebarSelected event : events) {
            event.menuSelected(index, indexSubMenu);
        }
    }

    public void addMenuItem(SidebarItemModel menu) {
        panelMenu.add(createMenuItem(menu), "h 35!");
    }

    public void addTitle(String title) {
        JLabel label = new JLabel(title);
        label.setForeground(UIManager.getColor("Label.disabledForeground"));
        label.setBorder(new EmptyBorder(15, 20, 5, 5));
        panelMenu.add(label);
    }

    public void addSpace(int size) {
        panelMenu.add(new JLabel(), "h " + size + "!");
    }

    public void addEmpty() {
        panelMenu.add(new JLabel(), "push");
    }

    public void setSelectedIndex(int index, int indexSubMenu) {
        for (Component com : panelMenu.getComponents()) {
            if (com instanceof SidebarItem item) {
                if (item.getIndex() == index) {
                    item.setSelectedIndex(indexSubMenu);
                    runEvent(index, indexSubMenu);
                    break;
                }
            }
        }
    }

    public void clearSelected() {
        for (Component com : panelMenu.getComponents()) {
            if (com instanceof SidebarItem item) {
                item.clearSelected();
            }
        }
    }

    public void addEvent(EventSidebarSelected event) {
        events.add(event);
    }

    private MigLayout menuLayout;
    private JPanel panelMenu;

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
