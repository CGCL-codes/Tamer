    public void init() throws PluginException {
        if (getProperty("filename") == null) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select output file");
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                setProperty("filename", fileChooser.getSelectedFile().getAbsolutePath());
            } else {
                raiseException("File selection canceled by user");
            }
        }
        try {
            _fileChannel = new FileOutputStream(getProperty("filename"), false).getChannel();
        } catch (FileNotFoundException e) {
            raiseException(e.getMessage());
        }
    }
