package com.fannog.cliente.ui.analista;

import com.fannog.cliente.utils.BeanFactory;
import com.fannog.cliente.utils.DataTableModel;
import com.fannog.servidor.DAO.ItrDAO;
import com.fannog.servidor.entities.Itr;
import com.fannog.servidor.exceptions.ServicioException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableRowSorter;

public class GestionItrs extends javax.swing.JPanel {

    private JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
    private DataTableModel<Itr> itrsTableModel = createItrsTableModel();
    private TableRowSorter<DataTableModel<Itr>> sorter = new TableRowSorter<>(itrsTableModel);

    public GestionItrs() {
        initComponents();

        tablaItr.getSelectionModel().addListSelectionListener(this::handleTableItrsSelection);

        try {
            loadItrs();
        } catch (Exception ex) {
        }
    }

    public void refreshTable() throws Exception {
        loadItrs();

        ((DataTableModel<Itr>) tablaItr.getModel()).fireTableDataChanged();
    }

    public void loadItrs() throws Exception {
        ItrDAO itrDAO = BeanFactory.local().lookup("Itr");
        List<Itr> itrs = itrDAO.findAll();

        itrsTableModel.setListRows(itrs);
        tablaItr.setModel(itrsTableModel);

        sorter = new TableRowSorter<>(itrsTableModel);

        tablaItr.setRowSorter(sorter);
    }

    public Itr getSelectedItr() {
        int modelRow = tablaItr.convertRowIndexToModel​(tablaItr.getSelectedRow());
        DataTableModel model = (DataTableModel) tablaItr.getModel();

        Itr itr = (Itr) model.getValueAt(modelRow, -1);

        return itr;
    }

    public DataTableModel createItrsTableModel() {

        String[] COLUMNS = {"NOMBRE", "ESTADO"};

        return new DataTableModel<Itr>(COLUMNS) {
            @Override
            public Object getValueAt(Itr itr, int columnas) {
                return switch (columnas) {
                    case -1 ->
                        itr;
                    case 0 ->
                        itr.getNombre();
                    case 1 ->
                        itr.isEliminado() ? "INACTIVO" : "ACTIVO";
                    default ->
                        null;
                };
            }
        };
    }

    private void enableBtnsCRUD(boolean enabled) {
        btnBajaOAlta.setEnabled(enabled);
        btnEditar.setEnabled(enabled);
    }

    public void handleTableItrsSelection(ListSelectionEvent e) {
        boolean seleccionValida = tablaItr.getSelectedRow() != -1;

        if (seleccionValida) {
            enableBtnsCRUD(true);

            changeBtnText(getSelectedItr().isEliminado());

            return;
        }

        enableBtnsCRUD(false);
    }

    private void changeBtnText(boolean isEliminado) {
        String btnText = isEliminado ? "Alta" : "Baja";
        btnBajaOAlta.setText(btnText);
    }

    private void filterTable() {
        RowFilter rw = null;

        List<RowFilter<Object, Object>> filters = new ArrayList<>(2);

        String estado = comboEstados.getSelectedItem().toString();
        String input = txtFiltro.getText().trim();

        try {
            if (!estado.equalsIgnoreCase("todos")) {
                estado = Pattern.quote(estado);
                String regex = String.format("^%s$", estado);

                filters.add(RowFilter.regexFilter(regex, 1));
            }

            if (!input.isEmpty()) {
                filters.add(RowFilter.regexFilter("(?i)" + input, 0));
            }

            rw = RowFilter.andFilter(filters);
        } catch (Exception e) {
            return;
        }

        sorter.setRowFilter(rw);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tablaItr = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        btnRefrescar = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnCrear = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        btnEditar = new javax.swing.JButton();
        btnBajaOAlta = new javax.swing.JButton();
        txtFiltro = new javax.swing.JTextField();
        comboEstados = new javax.swing.JComboBox<>();

        tablaItr.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tablaItr.setRowHeight(30);
        tablaItr.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tablaItr.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(tablaItr);

        jToolBar1.setRollover(true);

        btnRefrescar.setText("Refrescar");
        btnRefrescar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRefrescarMouseClicked(evt);
            }
        });
        btnRefrescar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefrescarActionPerformed(evt);
            }
        });
        jToolBar1.add(btnRefrescar);
        jToolBar1.add(jSeparator1);

        btnCrear.setText("Crear");
        btnCrear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCrearMouseClicked(evt);
            }
        });
        jToolBar1.add(btnCrear);
        jToolBar1.add(jSeparator2);

        btnEditar.setText("Editar");
        btnEditar.setEnabled(false);
        btnEditar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEditarMouseClicked(evt);
            }
        });
        jToolBar1.add(btnEditar);

        btnBajaOAlta.setText("Baja o alta");
        btnBajaOAlta.setEnabled(false);
        btnBajaOAlta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBajaOAltaMouseClicked(evt);
            }
        });
        btnBajaOAlta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBajaOAltaActionPerformed(evt);
            }
        });
        jToolBar1.add(btnBajaOAlta);

        txtFiltro.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                txtFiltroInputMethodTextChanged(evt);
            }
        });
        txtFiltro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFiltroKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFiltroKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtFiltroKeyTyped(evt);
            }
        });

        comboEstados.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "TODOS", "ACTIVO", "INACTIVO" }));
        comboEstados.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboEstadosItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtFiltro, javax.swing.GroupLayout.DEFAULT_SIZE, 776, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboEstados, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jToolBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtFiltro)
                    .addComponent(comboEstados, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnCrearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCrearMouseClicked
        new CreateOrEditITR(parentFrame, true, null, this).setVisible(true);
    }//GEN-LAST:event_btnCrearMouseClicked

    private void btnEditarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditarMouseClicked
        new CreateOrEditITR(parentFrame, true, getSelectedItr(), this).setVisible(true);

    }//GEN-LAST:event_btnEditarMouseClicked

    private void btnRefrescarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRefrescarMouseClicked

    }//GEN-LAST:event_btnRefrescarMouseClicked

    private void btnBajaOAltaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBajaOAltaMouseClicked
    }//GEN-LAST:event_btnBajaOAltaMouseClicked

    private void btnRefrescarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefrescarActionPerformed
        try {
            refreshTable();
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_btnRefrescarActionPerformed

    private void btnBajaOAltaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBajaOAltaActionPerformed
        boolean isEliminado = getSelectedItr().isEliminado();

        String accion = isEliminado ? "alta" : "baja";

        int confirmacion = JOptionPane.showConfirmDialog(null, "Por favor, confirme que desea realizar la " + accion + " del ITR", "Confirmar " + accion, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            Itr itr = getSelectedItr();

            ItrDAO itrDAO = BeanFactory.local().lookup("Itr");

            itr.setEliminado(!itr.isEliminado());

            itrDAO.edit(itr);

            changeBtnText(!isEliminado);
        } catch (ServicioException e) {
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_btnBajaOAltaActionPerformed

    private void txtFiltroInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_txtFiltroInputMethodTextChanged

    }//GEN-LAST:event_txtFiltroInputMethodTextChanged

    private void txtFiltroKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltroKeyPressed

    }//GEN-LAST:event_txtFiltroKeyPressed

    private void txtFiltroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltroKeyReleased

    }//GEN-LAST:event_txtFiltroKeyReleased

    private void txtFiltroKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltroKeyTyped
        filterTable();
    }//GEN-LAST:event_txtFiltroKeyTyped

    private void comboEstadosItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboEstadosItemStateChanged
        filterTable();
    }//GEN-LAST:event_comboEstadosItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBajaOAlta;
    private javax.swing.JButton btnCrear;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnRefrescar;
    private javax.swing.JComboBox<String> comboEstados;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTable tablaItr;
    private javax.swing.JTextField txtFiltro;
    // End of variables declaration//GEN-END:variables
}
