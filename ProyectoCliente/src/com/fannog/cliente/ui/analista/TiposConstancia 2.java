package com.fannog.cliente.ui.analista;

import com.fannog.cliente.utils.BeanFactory;
import com.fannog.cliente.utils.DataTableModel;
import com.fannog.servidor.DAO.TipoConstanciaDAO;
import com.fannog.servidor.entities.TipoConstancia;
import com.fannog.servidor.exceptions.ServicioException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;

public class TiposConstancia extends javax.swing.JPanel {

    private DataTableModel<TipoConstancia> tableModel = createTipoConstanciaModel();
    TableRowSorter<DataTableModel<TipoConstancia>> sorter = new TableRowSorter<>(tableModel);

    private JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

    public TiposConstancia() {
        initComponents();
        enableOperations(false);

        tableTiposConstancia.getSelectionModel().addListSelectionListener(this::handleTableConstanciaSelectionEvent);

        ((DefaultTableCellRenderer) tableTiposConstancia.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.LEFT);

        try {
            loadTiposConstancia();
        } catch (Exception ex) {
        }
    }

    public JFrame getParentFrame() {
        return parentFrame;
    }
    
    public void refreshTable() throws Exception {
        loadTiposConstancia();
        
        ((DataTableModel<TipoConstancia>) tableTiposConstancia.getModel()).fireTableDataChanged();
    }

    private void enableOperations(boolean enable) {
        btnActualizar.setEnabled(enable);
        btnBajaOAlta.setEnabled(enable);
        btnDownloadPlantilla.setEnabled(enable);
    }

    public void handleTableConstanciaSelectionEvent(ListSelectionEvent e) {
        boolean seleccionValida = tableTiposConstancia.getSelectedRow() != -1;

        if (seleccionValida) {
            changeBtnText(getSelectedTipo().isEliminado());
        }

        enableOperations(seleccionValida);
    }

    private void changeBtnText(boolean isEliminado) {
        String btnText = isEliminado ? "Alta" : "Baja";
        btnBajaOAlta.setText(btnText);
    }

    public void loadTiposConstancia() throws Exception {
        TipoConstanciaDAO tipoDAO = BeanFactory.local().lookup("TipoConstancia");

        List<TipoConstancia> tipos = tipoDAO.findAll();
        tableModel.setListRows(tipos);
        tableTiposConstancia.setModel(tableModel);

        sorter = new TableRowSorter<>(tableModel);

        tableTiposConstancia.setRowSorter(sorter);
    }

    private TipoConstancia getSelectedTipo() {
        int modelRow = tableTiposConstancia.convertRowIndexToModel​(tableTiposConstancia.getSelectedRow());
        DataTableModel model = (DataTableModel) tableTiposConstancia.getModel();

        TipoConstancia tipo = (TipoConstancia) model.getValueAt(modelRow, -1);

        return tipo;
    }

    public DataTableModel createTipoConstanciaModel() {
        String[] COLUMNS = {"NOMBRE", "ESTADO"};

        return new DataTableModel<TipoConstancia>(COLUMNS) {
            @Override
            public Object getValueAt(TipoConstancia tipo, int columnas) {
                return switch (columnas) {
                    case -1 ->
                        tipo;
                    case 0 ->
                        tipo.getNombre();
                    case 1 ->
                        tipo.isEliminado() ? "INACTIVO" : "ACTIVO";
                    default ->
                        null;
                };
            }
        };
    }

    private void downloadPlantilla() {
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("*.doc", "doc"));
        fileChooser.setDialogTitle("Descargar archivo");

        int option = fileChooser.showSaveDialog(parentFrame);

        if (option == JFileChooser.APPROVE_OPTION) {
            String strPath = fileChooser.getSelectedFile().getAbsolutePath();

            Path path = new File(strPath).toPath();

            byte[] fileContent = getSelectedTipo().getPlantilla();

            try {
                Files.write(path, fileContent);
            } catch (IOException ex) {
            }
        }
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
        tableTiposConstancia = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        btnNuevo = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnDownloadPlantilla = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnBajaOAlta = new javax.swing.JButton();
        txtFiltro = new javax.swing.JTextField();
        comboEstados = new javax.swing.JComboBox<>();

        tableTiposConstancia.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tableTiposConstancia.setRowHeight(30);
        tableTiposConstancia.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableTiposConstancia.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableTiposConstancia.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tableTiposConstancia);

        jToolBar1.setRollover(true);

        btnNuevo.setText("Nuevo");
        btnNuevo.setFocusable(false);
        btnNuevo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNuevo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });
        jToolBar1.add(btnNuevo);
        jToolBar1.add(jSeparator1);

        btnDownloadPlantilla.setText("Descargar plantilla");
        btnDownloadPlantilla.setFocusable(false);
        btnDownloadPlantilla.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDownloadPlantilla.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDownloadPlantilla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDownloadPlantillaActionPerformed(evt);
            }
        });
        jToolBar1.add(btnDownloadPlantilla);

        btnActualizar.setText("Actualizar");
        btnActualizar.setFocusable(false);
        btnActualizar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnActualizar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });
        jToolBar1.add(btnActualizar);

        btnBajaOAlta.setText("Alta o baja");
        btnBajaOAlta.setFocusable(false);
        btnBajaOAlta.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBajaOAlta.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
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
                    .addComponent(jToolBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 766, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtFiltro)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboEstados, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtFiltro, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                    .addComponent(comboEstados))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnDownloadPlantillaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDownloadPlantillaActionPerformed
        downloadPlantilla();
    }//GEN-LAST:event_btnDownloadPlantillaActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        new CreateOrEditTipoConstancia(parentFrame, true, getSelectedTipo(), this).setVisible(true);
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        new CreateOrEditTipoConstancia(parentFrame, true, null, this).setVisible(true);
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void comboEstadosItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboEstadosItemStateChanged
        filterTable();
    }//GEN-LAST:event_comboEstadosItemStateChanged

    private void txtFiltroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltroKeyReleased

    }//GEN-LAST:event_txtFiltroKeyReleased

    private void txtFiltroKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltroKeyPressed
    }//GEN-LAST:event_txtFiltroKeyPressed

    private void txtFiltroInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_txtFiltroInputMethodTextChanged

    }//GEN-LAST:event_txtFiltroInputMethodTextChanged

    private void txtFiltroKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltroKeyTyped
        filterTable();
    }//GEN-LAST:event_txtFiltroKeyTyped

    private void btnBajaOAltaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBajaOAltaActionPerformed
        TipoConstancia tipo = getSelectedTipo();

        String accion = tipo.isEliminado() ? "alta" : "baja";

        int confirmacion = JOptionPane.showConfirmDialog(null, "Por favor, confirme que desea realizar la " + accion + " del tipo de constancia", "Confirmar " + accion, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            TipoConstanciaDAO tipoDAO = BeanFactory.local().lookup("TipoConstancia");

            tipo.setEliminado(!tipo.isEliminado());

            tipoDAO.edit(tipo, null);

            changeBtnText(!tipo.isEliminado());
        } catch (ServicioException e) {
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_btnBajaOAltaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnBajaOAlta;
    private javax.swing.JButton btnDownloadPlantilla;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JComboBox<String> comboEstados;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTable tableTiposConstancia;
    private javax.swing.JTextField txtFiltro;
    // End of variables declaration//GEN-END:variables
}
