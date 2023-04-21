    public void doExportConfig() {
        if (configFileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File configFile = configFileChooser.getSelectedFile();
            if (!configFile.exists() || JOptionPane.showConfirmDialog(this, String.format(MessageBundle.getMessage("already_exists_overwrite_file"), configFile.getName()), MessageBundle.getMessage("confirm"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                FileOutputStream fout = null;
                try {
                    fout = new FileOutputStream(configFile);
                    this.rdbaConfig.storeToXML(fout, "");
                } catch (IOException e) {
                    ExceptionHandler.warn(e);
                    JOptionPane.showMessageDialog(this, e.getMessage());
                } finally {
                    if (fout != null) {
                        try {
                            fout.close();
                        } catch (IOException e2) {
                            ExceptionHandler.warn(e2);
                        }
                    }
                }
            }
        }
    }
