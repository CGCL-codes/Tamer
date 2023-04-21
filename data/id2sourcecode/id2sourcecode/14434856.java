    private void saveFileAction(ActionEvent evt) {
        try {
            if (jTable1.getSelectedRows().length > 0) {
                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                for (int i : jTable1.getSelectedRows()) {
                    JFileChooser jfc;
                    if (lastOutputDir != null) {
                        jfc = new JFileChooser(lastOutputDir);
                    } else {
                        jfc = new JFileChooser();
                    }
                    jfc.setMultiSelectionEnabled(false);
                    int idx = sorter.convertRowIndexToModel(i);
                    String fileName = (String) model.getValueAt(idx, 0);
                    File file = new File(fileName);
                    jfc.setSelectedFile(file);
                    if (jfc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                        file = jfc.getSelectedFile();
                        Integer option = null;
                        if (file.exists()) {
                            option = JOptionPane.showConfirmDialog(this, "File already exists, overwrite?", "Overwrite existing file", JOptionPane.YES_NO_OPTION);
                        }
                        if (option == null || option == JOptionPane.YES_OPTION) {
                            byte[] data = getFileData(fileName);
                            FSUtils.writeFile(file, data);
                            lastOutputDir = file.getParentFile();
                        }
                    }
                }
            }
        } catch (Exception e) {
            FrontEnd.displayErrorMessage("Failed to save file!", e);
        }
    }
