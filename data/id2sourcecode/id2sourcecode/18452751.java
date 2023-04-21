    private void saveAs() throws IOException {
        JFileChooser chooser = new JFileChooser();
        if (currentDirectory != null) chooser.setCurrentDirectory(currentDirectory);
        chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {

            public boolean accept(File f) {
                return f.getName().endsWith(".cmap") || f.isDirectory();
            }

            public String getDescription() {
                return "IENJINIA Character Map";
            }
        });
        int returnVal = chooser.showSaveDialog(this);
        if (returnVal != JFileChooser.APPROVE_OPTION) return;
        File f = chooser.getSelectedFile();
        if (f.exists()) {
            int option = JOptionPane.showConfirmDialog(this, "There is already a file with that name. " + "Overwrite?", "File with same name exists!", JOptionPane.YES_NO_OPTION);
            if (option != JOptionPane.YES_OPTION) return;
        }
        currentDirectory = f.getParentFile();
        String path = f.getAbsolutePath();
        if (!path.endsWith(".cmap")) path += ".cmap";
        charData.writeData(path);
        filename = f.getName();
        if (!filename.endsWith(".cmap")) filename += ".cmap";
        saveBtn.setEnabled(true);
    }
