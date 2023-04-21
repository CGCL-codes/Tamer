    public void actionPerformed(ActionEvent e) {
        Workbook workbook = uiController.getActiveWorkbook();
        File oldFile = app.getFile(workbook);
        File file = null;
        boolean promptForFile = true;
        while (promptForFile) {
            file = chooser.getFileToSave();
            if (file != null) {
                if (file.exists() && (oldFile == null || !file.equals(oldFile))) {
                    int option = JOptionPane.showConfirmDialog(null, "The chosen file " + file + " already exists\n" + "Do you want to overwrite it?", "Replace existing file?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (option == JOptionPane.YES_OPTION) promptForFile = false; else if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) return;
                } else promptForFile = false;
            } else return;
        }
        try {
            app.saveAs(workbook, file);
        } catch (IOException ex) {
            showErrorDialog("An I/O error occurred when saving the file.");
            return;
        }
    }
