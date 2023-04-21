    protected boolean saveFileAsDialog(FileEditorFile file) {
        JFileChooser jfc = new JFileChooser();
        jfc.setSelectedFile(file.getFile());
        for (javax.swing.filechooser.FileFilter filter : file.getSaveFileFilters()) {
            jfc.addChoosableFileFilter(filter);
        }
        if (jfc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selected = jfc.getSelectedFile();
            currentDirectory = selected.getParentFile();
            if (selected.exists()) {
                if (JOptionPane.showConfirmDialog(this, "This file already exists. Overwrite?", "Overwrite", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) != JOptionPane.OK_OPTION) {
                    return false;
                }
            }
            file.setFile(jfc.getSelectedFile());
            try {
                saveFile(file);
                file.setUnsavedChanges(false);
                return true;
            } catch (FileEditorFileException e) {
                showError("Failed to save file: " + e.getMessage());
                return false;
            }
        } else {
            return false;
        }
    }
