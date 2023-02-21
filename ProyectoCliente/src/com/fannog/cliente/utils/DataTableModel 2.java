package com.fannog.cliente.utils;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public abstract class DataTableModel<T> extends AbstractTableModel {

    protected String[] COLUMNS;

    public DataTableModel(String[] COLUMNS) {
        this.COLUMNS = COLUMNS;
    }

    private List<T> rows = new ArrayList<>();

    public List<T> getListRows() {
        return rows;
    }

    public void setListRows(List<T> listadoDeFilas) {
        this.rows = listadoDeFilas;
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        T t = rows.get(rowIndex);

        return getValueAt(t, columnIndex);
    }

    public abstract Object getValueAt(T t, int columnas);

    @Override
    public String getColumnName(int column) {
        return COLUMNS[column];
    }

    @Override
    public int getColumnCount() {
        return COLUMNS.length;
    }
}
