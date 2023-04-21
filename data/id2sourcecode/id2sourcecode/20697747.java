    public void init() throws PluginException {
        if (!getProperty("inputMode").equals(INPUT_MODE_ALL) && !getProperty("inputMode").equals(INPUT_MODE_FIXED) && !getProperty("inputMode").equals(INPUT_MODE_DELIMITER)) {
            raiseException("Invalid input mode");
        }
        if (getProperty("filename") == null) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select input file");
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                setProperty("filename", fileChooser.getSelectedFile().getAbsolutePath());
            } else {
                raiseException("File selection canceled by user");
            }
        }
        try {
            _fileInputStream = new FileInputStream(getProperty("filename"));
        } catch (FileNotFoundException e) {
            raiseException(e.getMessage());
        }
        if (getProperty("readBufferSize") != null) {
            _readBufferSize = Integer.parseInt(getProperty("readBufferSize"));
        } else {
            _readBufferSize = DEFAULT_READBUFFER_SIZE;
        }
        _in = _fileInputStream.getChannel();
        _bytesRemaining = (int) new java.io.File(getProperty("filename")).length();
    }
