    private void exportCsvButtonActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser jfc = new JFileChooser(".");
        int saveDialogResult = jfc.showSaveDialog(this);
        if (saveDialogResult == JOptionPane.OK_OPTION) {
            if (jfc.getSelectedFile() != null && jfc.getSelectedFile().exists()) {
                int result = JOptionPane.showConfirmDialog(null, "The file " + jfc.getSelectedFile().getName() + " already exists, overwrite it?", "Confirm Overwrite", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (result == JOptionPane.CANCEL_OPTION) {
                    return;
                }
            }
            CSVResultsExporter cve = new CSVResultsExporter(nr, jfc.getSelectedFile());
            cve.save();
        }
    }
