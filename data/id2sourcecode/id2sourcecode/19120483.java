    public void deleteLine(int row) {
        if (m_order != null && row != -1) {
            MOrderLine[] lineas = m_order.getLines();
            int numLineas = lineas.length;
            if (numLineas > row) {
                lineas[row].delete(true);
                for (int i = row; i < (numLineas - 1); i++) lineas[i] = lineas[i + 1];
                lineas[numLineas - 1] = null;
            }
        }
    }
