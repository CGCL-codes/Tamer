    public File showSaveFileDialog(File lastFile) {
        JFileChooser fileChooser;
        fileChooser = new JFileChooser(lastFile);
        int result = fileChooser.showSaveDialog(window);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (file.exists()) {
                int overwriteResult = JOptionPane.showConfirmDialog(window, "The file already exists, would you like to overwrite it?");
                if (overwriteResult != JOptionPane.OK_OPTION) {
                    showWarningDialog("File will not be overwritten.");
                    return null;
                }
            }
            return file;
        }
        return null;
    }
