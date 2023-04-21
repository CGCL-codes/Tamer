    private void initBasePaths() {
        readbasePath = pathRelativeToAbsolute(systemProp.getProperty("readbase"), userdir);
        if (readbasePath != null && !isFileValid(readbasePath, true, false, false, true)) {
            throw new RuntimeException("System.property.readbase is invalid (" + readbasePath + ")");
        }
        if (readbasePath == null) {
            readbasePath = userdir + slash + "vicaya";
            if (!isFileValid(readbasePath, true, false, false, true)) {
                readbasePath = null;
            }
        }
        if (readbasePath == null) {
            readbasePath = userdir;
            if (!isFileValid(readbasePath, true, false, false, true)) {
                readbasePath = null;
            }
        }
        if (readbasePath == null) {
            throw new RuntimeException("Can not locate a suitable readbase directory");
        }
        readbaseConfigProp = loadProperties(readbasePath + slash + "config.properties");
        writebasePath = pathRelativeToAbsolute(systemProp.getProperty("writebase"), userdir);
        if (writebasePath != null && !isFileValid(writebasePath, true, true, false, true)) {
            throw new RuntimeException("System.property.writebase is invalid (" + writebasePath + ")");
        }
        if (writebasePath == null) {
            writebasePath = pathRelativeToAbsolute(readbaseConfigProp.getProperty("writebase"), readbasePath);
            if (!isFileValid(writebasePath, true, true, false, true)) {
                writebasePath = null;
            }
        }
        if (writebasePath == null) {
            writebasePath = userhome + slash + ".vicaya";
            if (!isFileValid(writebasePath, true, true, false, true)) {
                writebasePath = null;
            }
        }
        if (writebasePath == null) {
            throw new RuntimeException("Can not locate a suitable writebase directory");
        }
        writebaseConfigProp = loadProperties(writebasePath + slash + "config.properties");
    }
