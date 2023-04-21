    public void showAndHandleSaveToFileDialog() {
        JFileChooser chooser = new JFileChooser();
        String[] modelfiles = new String[] { "xml", "XML" };
        chooser.setFileFilter(new SimpleFileFilter(modelfiles, "*.xml"));
        int option = chooser.showSaveDialog(maf);
        if (option == JFileChooser.APPROVE_OPTION) {
            if (chooser.getSelectedFile() != null) {
                File file = chooser.getSelectedFile();
                String fileName = file.getAbsolutePath();
                if (!fileName.endsWith(".xml")) {
                    fileName = fileName.concat(".xml");
                    file = new File(fileName);
                }
                if (file.exists()) {
                    int exit = JOptionPane.showConfirmDialog(chooser, "File already exists. Overwrite?", "File already exists. Overwrite?", JOptionPane.YES_NO_OPTION);
                    if (exit == JOptionPane.YES_OPTION) {
                        saveToSpecifiedFileAndSetAsCurrentDefaultFile(file);
                    }
                } else {
                    saveToSpecifiedFileAndSetAsCurrentDefaultFile(file);
                }
            }
        }
    }
