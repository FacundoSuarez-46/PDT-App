package com.fannog.cliente.sidebar;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.UIManager;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;

public class SidebarItem extends javax.swing.JPanel {

    private final List<EventSidebarSelected> events = new ArrayList<>();
    private final int index;
    private final boolean hasSubMenu;
    private Animator animator;
    private int buttonAngle = -1;
    private boolean open;

    public SidebarItem(SidebarItemModel item, int index, MigLayout layout) {
        this.index = index;
        this.hasSubMenu = item.getSubMenu().length > 0;

        init(item);

        if (hasSubMenu) {
            initAnimator(layout);
            buttonAngle = 0;
        }
    }

    private void init(SidebarItemModel item) {
        setOpaque(false);
        setLayout(new MigLayout("wrap,fillx,inset 0", "[fill]", "[fill,35!]" + (hasSubMenu ? "0[fill,30!]" : "")));

        Item menu = new Item(true, 0);
        menu.setText("  " + item.getMenuName());

        menu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setForeground(menu.getMainColor());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!menu.isSelected()) {
                    setForeground(UIManager.getColor("Label.disabledForeground"));
                }
            }
        });

        menu.addActionListener((ActionEvent arg0) -> {
            runEvent(index, 0);
        });

        if (hasSubMenu) {
            menu.addActionListener((ActionEvent arg0) -> {
                open = !open;
                startAnimator();
            });
        }
        add(menu);
        int subIndex = 0;
        
        for (String subMenu : item.getSubMenu()) {
            Item sMenu = new Item(false, ++subIndex);
            sMenu.setText(subMenu);
            sMenu.addActionListener((ActionEvent arg0) -> {
                runEvent(index, sMenu.getIndex());
            });
            add(sMenu);
        }
    }

    private void initAnimator(MigLayout layout) {
        animator = new Animator(300, new TimingTargetAdapter() {
            private int height;

            @Override
            public void begin() {
                height = getPreferredSize().height - 35;
            }

            @Override
            public void timingEvent(float fraction) {
                float f = open ? fraction : 1f - fraction;
                int s = (int) (35 + f * height);
                layout.setComponentConstraints(SidebarItem.this, "h " + s + "!");
                buttonAngle = (int) (f * 180);
                revalidate();
                repaint();
            }
        });
        animator.setResolution(0);
        animator.setDeceleration(.5f);
        animator.setAcceleration(.5f);
    }

    private void startAnimator() {
        if (animator.isRunning()) {
            float f = animator.getTimingFraction();
            animator.stop();
            animator.setStartFraction(1f - f);
        } else {
            animator.setStartFraction(0f);
        }
        animator.start();
    }

    public void addEvent(EventSidebarSelected event) {
        this.events.add(event);
    }

    private void runEvent(int index, int subIndex) {
        for (EventSidebarSelected evnet : events) {
            evnet.menuSelected(index, subIndex);
        }
    }

    public int getIndex() {
        return index;
    }

    public boolean isHasSubMenu() {
        return hasSubMenu;
    }

    public void clearSelected() {
        setForeground(new Color(170, 170, 170));

        for (Component com : getComponents()) {
            Item item = (Item) com;
            item.setSelected(false);
        }
    }

    public void setSelectedIndex(int index) {
        for (Component com : getComponents()) {
            Item item = (Item) com;

            if (item.isMainMenu()) {
                item.setSelected(true);
                setForeground(item.getMainColor());
            }

            if (item.getIndex() == index) {
                item.setSelected(true);
                break;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (buttonAngle >= 0) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getForeground());
            int x = getWidth() - 25;
            int y = 15;
            Path2D p2 = new Path2D.Double();
            p2.moveTo(x, y);
            p2.lineTo(x + 4, y + 4);
            p2.lineTo(x + 8, y);
            AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(buttonAngle), x + 4, y + 2);
            g2.setStroke(new BasicStroke(1.8f));
            g2.draw(at.createTransformedShape(p2));
            g2.dispose();
        }
    }

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
