    private void ok() {
        File file = new File(dataFileTextField.getText());
        if (file.exists()) {
            int overwriteReturn = JOptionPane.showConfirmDialog(null, file.getName() + " already exists. Do you want to overwrite it?", "Overwrite file?", JOptionPane.YES_NO_OPTION);
            if (overwriteReturn == JOptionPane.NO_OPTION) {
                return;
            }
        }
        dispose();
    }
