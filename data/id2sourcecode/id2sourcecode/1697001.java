    public void newArchive() {
        fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new SimpleFileFilter("zip", "ZIP Files"));
        fileChooser.addChoosableFileFilter(new SimpleFileFilter("jar", "JAR Files"));
        fileChooser.setDialogTitle("Create a New Archive");
        fileChooser.addChoosableFileFilter(new ArchiveFileFilter());
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileSelectionMode(0);
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooser.setCurrentDirectory(workingDir);
        if (fileChooser.showDialog(this, "Create a new archive") == JFileChooser.CANCEL_OPTION) {
            return;
        }
        zipFile.setCurrentArchive(fileChooser.getSelectedFile().getAbsolutePath());
        workingDir = fileChooser.getSelectedFile().getParentFile();
        if (zipFile.fileExists()) {
            int j = JOptionPane.showConfirmDialog(this, "This file exists already. \nDo you want to overwrite existing one?", "Warning", 0, 2, null);
            if (j == JOptionPane.OK_OPTION) {
                zipFile.fileDelete();
            }
        }
        this.addFiles();
        updateMenuToolbarState();
    }
