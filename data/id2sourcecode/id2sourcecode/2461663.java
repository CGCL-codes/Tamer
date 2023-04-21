    void saveConfigFile() {
        if (chooser == null) initChooser();
        chooser.setFileFilter(file_filter);
        int retval = chooser.showSaveDialog(this);
        if (retval == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (file != null) {
                String filename = file.toString();
                if (!filename.endsWith("." + FileExt[0])) {
                    filename += "." + FileExt[0];
                    file = new File(filename);
                }
                if (file.exists()) {
                    if (JOptionPane.showConfirmDialog(this, "File " + filename + "\nAlready exists !     Overwrite it  ?", "Overwrite ?", JOptionPane.YES_NO_OPTION) != JOptionPane.OK_OPTION) return;
                }
                writeConfig(filename);
            }
        }
    }
