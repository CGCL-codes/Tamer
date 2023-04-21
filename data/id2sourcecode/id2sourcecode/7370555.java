    public static File selectFileToSave(String extDescription, String extension, File lastDir, File lastFile) {
        JFileChooser fc = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(extDescription, extension);
        fc.setFileFilter(filter);
        fc.setCurrentDirectory(lastDir);
        if (lastFile != null) {
            fc.setSelectedFile(lastFile);
        }
        int returnVal = fc.showSaveDialog(null);
        File file = null;
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = fc.getSelectedFile();
            String fname = file.getPath();
            if (!fname.endsWith("." + extension)) {
                file = new File(fname.concat("." + extension));
            }
            if (file.exists()) {
                if (file.canWrite()) {
                    int choice = JOptionPane.showConfirmDialog(null, "This file already exists--do you want to overwrite it?", "Overwrite existing file?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (choice != JOptionPane.YES_OPTION) {
                        file = null;
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "This file already exists and is read-only; we can't overwrite it.", "Can't save!", JOptionPane.ERROR_MESSAGE);
                    file = null;
                }
            }
        }
        return (file);
    }
