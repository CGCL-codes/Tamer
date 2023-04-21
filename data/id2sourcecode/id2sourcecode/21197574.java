    private void exportXmlButtonActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser jfc = new JFileChooser(".");
        int saveDialogResult = jfc.showSaveDialog(this);
        if (saveDialogResult == JOptionPane.OK_OPTION) {
            if (jfc.getSelectedFile() != null && jfc.getSelectedFile().exists()) {
                int result = JOptionPane.showConfirmDialog(null, "The file " + jfc.getSelectedFile().getName() + " already exists, overwrite it?", "Confirm Overwrite", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (result == JOptionPane.CANCEL_OPTION) {
                    return;
                }
            }
            try {
                XMLResultsExporter xre = new XMLResultsExporter(jfc.getSelectedFile());
                xre.export(nr, nd);
                System.out.println("Exported XML:\n------------------------");
                System.out.print(XMLStringResultsExporter.export(nr, nd));
                System.out.println("------------------------");
            } catch (IOException e) {
                System.out.println("XML Export failed: " + e.getMessage());
            }
        }
    }
