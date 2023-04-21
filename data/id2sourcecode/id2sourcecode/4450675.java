    protected void exportToFile() {
        try {
            File fichier = new File(tableExporterPanel.getExportFileName());
            FileOutputStream fos = new FileOutputStream(fichier);
            FileChannel fc = fos.getChannel();
            ByteBuffer bb = ByteBuffer.allocateDirect(1024 * 1024);
            String charsetName = tableExporterPanel.getCharsetName();
            logger.debug("Using charset : " + charsetName);
            byte[] cellSeparator = tableExporterPanel.getCellSeparator().getBytes(charsetName);
            byte[] rowSeparator = tableExporterPanel.getRowSeparator().getBytes(charsetName);
            byte[] carriageReturn = "\n".getBytes(charsetName);
            boolean useCellSeparator = tableExporterPanel.isCellSeparated();
            boolean useRowSeparator = tableExporterPanel.isRowSeparated();
            boolean newLineForEachCell = tableExporterPanel.isNewLineForEachCell();
            boolean newLineForEachRow = tableExporterPanel.isNewLineForEachRow();
            int[] selectedRows;
            if (tableExporterPanel.isOnlySelectedRows()) {
                selectedRows = tableToExport.getSelectedRows();
            } else {
                selectedRows = new int[tableToExport.getRowCount()];
                for (int cptRow = 0; cptRow < selectedRows.length; cptRow++) {
                    selectedRows[cptRow] = cptRow;
                }
            }
            fc.position(0);
            for (int cptRow = 0; cptRow < selectedRows.length; cptRow++) {
                if (tableExporterPanel.isCancelAsked()) {
                    logger.warn("Cancel asked while exporting to file. Stoped at row #" + cptRow + " out of " + selectedRows.length);
                    break;
                }
                bb.clear();
                Object value = null;
                String valueStr = null;
                if (tableExporterPanel.isIncludeHeaders()) {
                    int columnCount = tableToExport.getColumnCount();
                    for (int cptCol = 0; cptCol < columnCount; cptCol++) {
                        bb.put(tableToExport.getColumnName(cptCol).getBytes(charsetName));
                        if (useCellSeparator) {
                            bb.put(cellSeparator);
                        }
                        if (newLineForEachCell) {
                            bb.put(carriageReturn);
                        }
                    }
                    if (useRowSeparator) {
                        bb.put(rowSeparator);
                    }
                    if (newLineForEachRow) {
                        bb.put(carriageReturn);
                    }
                }
                bb.flip();
                fc.write(bb);
                bb.clear();
                for (int cptCol = 0; cptCol < tableToExport.getColumnCount(); cptCol++) {
                    value = tableToExport.getValueAt(selectedRows[cptRow], cptCol);
                    if (value != null) {
                        valueStr = value.toString();
                    } else {
                        valueStr = "null";
                    }
                    bb.put(valueStr.getBytes(charsetName));
                    if (useCellSeparator) {
                        bb.put(cellSeparator);
                    }
                    if (newLineForEachCell) {
                        bb.put(carriageReturn);
                    }
                    bb.flip();
                    fc.write(bb);
                    bb.clear();
                    tableExporterPanel.setProgress(++currentTask);
                }
                if (useRowSeparator) {
                    bb.put(rowSeparator);
                }
                if (newLineForEachRow) {
                    bb.put(carriageReturn);
                }
                bb.flip();
                fc.write(bb);
                bb.clear();
            }
            fc.close();
            fos.close();
            JOptionPane.showMessageDialog(tableExporterPanel, "Exported " + selectedRows.length + " rows", "Yeah...", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ioe) {
            Component parent = null;
            if (tableExporterPanel.getParentInternalFrame() != null) {
                parent = tableExporterPanel.getParentInternalFrame().getDesktopPane();
            }
            if (tableExporterPanel.getParentFrame() != null) {
                parent = tableExporterPanel.getParentFrame();
            } else {
                parent = tableExporterPanel.getParent();
            }
            JOptionPane.showInternalMessageDialog(parent, "IOException : " + ioe.getMessage(), "Ohoh...", JOptionPane.ERROR_MESSAGE);
            logger.error("Cannot export to file.", ioe);
        }
    }
