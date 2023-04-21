    public RTFDocumentSet read(final File rtfFile, final EventListener listener) throws IOException {
        if (rtfFile == null || !rtfFile.exists()) {
            throw new IOException("RTF File cannot be located");
        }
        if (getTmpDir() == null) {
            throw new IOException("Tmp dir is not initialized");
        }
        String name = rtfFile.getName();
        name = name.substring(0, name.lastIndexOf("."));
        listener.message("Phase (1) Moving Files to temporary directory");
        final File propFile = new File(rtfFile.getParentFile().getAbsolutePath() + File.separator + name + ".properties");
        if (propFile == null || !propFile.exists()) {
            listener.message("No properties file found. Creating file with default properties.");
            createDefaultProperties(propFile);
        } else {
            Properties properties = new Properties();
            FileInputStream fis = new FileInputStream(propFile);
            properties.load(fis);
            if (properties.getProperty("authors") == null) {
                listener.message("Missing authors property. Default properties file created.");
                fis.close();
                createDefaultProperties(propFile);
            }
        }
        final File tempDir = new File(getTmpDir() + File.separator + name + "_copy");
        tempDir.mkdir();
        final File tempRtfFile = new File(tempDir.getAbsolutePath() + File.separator + name + ".rtf");
        final File tempPropFile = new File(tempDir.getAbsolutePath() + File.separator + name + ".properties");
        FileUtils.copyFile(rtfFile, tempRtfFile);
        FileUtils.copyFile(propFile, tempPropFile);
        final ViolationCollatingErrorHandler handler = new ViolationCollatingErrorHandler();
        return new RTFDocumentSet(name, tempRtfFile, listener, handler);
    }
