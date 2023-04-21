    public boolean askFileName() {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.showSaveDialog(this);
        File f = jFileChooser.getSelectedFile();
        logger.debug(f);
        if (f != null) {
            if (f.exists()) {
                if (JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(this, f.getName() + " already exists. Are you sure to overwrite this file?", "File exists!", JOptionPane.YES_NO_OPTION)) {
                    return false;
                }
            }
            try {
                setFile(VFS.resolveFile(f.getAbsolutePath()));
                return true;
            } catch (Exception ex) {
                logger.error(ex, ex);
            }
        }
        return false;
    }
