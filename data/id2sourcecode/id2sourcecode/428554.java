    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("simcheck")) {
            try {
                d_m.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                makeIndex();
                searchTableIndex();
            } finally {
                d_m.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                d_m.dispose();
            }
        }
        if (command.equals("mcancel")) {
            if (isRowSet == true && _rows != null) _rows.close();
            d_m.dispose();
        }
        if (command.equals("cancel")) {
            if (isRowSet == true && _rows != null) _rows.close();
            dg.dispose();
        }
        if (command.equals("help")) {
            JOptionPane.showMessageDialog(null, "Open Similarity_check.[doc][pdf] to get more Infomation");
            return;
        }
        if (command.equals("delete")) {
            if (outputRT.isSorting()) {
                JOptionPane.showMessageDialog(null, "Table is in Sorting State.");
                return;
            }
            if (isChanged == true && chk.isSelected() == true) {
                JOptionPane.showMessageDialog(null, "Parent table has changed.\n Redo Similarity Check to get updated value.");
                ConsoleFrame.addText("\n Parent table has changed.\n Redo Similarity Check to get updated value.");
                return;
            }
            markDel = new Vector<Integer>();
            int rowC = outputRT.table.getRowCount();
            for (int i = 0; i < rowC; i++) if (outputRT.getValueAt(i, 0) != null && ((Boolean) outputRT.getValueAt(i, 0)).booleanValue() == true) markDel.add(i);
            int size = markDel.size();
            if (size == 0) {
                JOptionPane.showMessageDialog(null, "Check Rows to Delete");
                return;
            }
            int n = JOptionPane.showConfirmDialog(null, "Do you want to delete " + size + " rows?", "Confirmation Type", JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.NO_OPTION) return;
            int[] parentO = new int[size];
            for (int i = 0; i < size; i++) {
                if (chk.isSelected() == true) {
                    if (parentMap.get(markDel.get(size - 1 - i)) == null) parentO[i] = -1; else parentO[i] = parentMap.get(markDel.get(size - 1 - i));
                }
                outputRT.removeRows(markDel.get(size - 1 - i), 1);
            }
            dg.repaint();
            if (chk.isSelected() == true) {
                if (isRowSet == false) _rt.cancelSorting();
                Arrays.sort(parentO);
                int indexL = parentO.length;
                for (int i = 0; i < indexL; i++) {
                    if (parentO[indexL - 1 - i] < 0) continue;
                    if (indexL - 1 - i > 0) {
                        if (parentO[indexL - 1 - i] > parentO[indexL - 1 - (i + 1)]) if (isRowSet == false) _rt.removeRows(parentO[indexL - 1 - i], 1); else _rows.deleteRow(parentO[indexL - 1 - i]); else continue;
                    } else {
                        if (isRowSet == false) _rt.removeRows(parentO[indexL - 1 - i], 1); else _rows.deleteRow(parentO[indexL - 1 - i]);
                    }
                }
                if (isRowSet == false) _rt.repaint(); else if (_rows != null) _rows.close();
            }
            chk.setSelected(false);
            chk.setEnabled(false);
        }
    }
