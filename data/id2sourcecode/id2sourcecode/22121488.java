    public void configure() {
        File propertyDirectory = PropertyProvider.getProvider().getPropertyManager().getPropertyDirectory();
        String propertyFile = getFileName();
        try {
            if (propertyFile == null) {
                LogManager.getLogManager().readConfiguration();
                return;
            }
            File configFile = new File(propertyDirectory, propertyFile);
            if (configFile.exists()) {
                InputStream in = new FileInputStream(configFile.getCanonicalPath());
                BufferedInputStream bin = new BufferedInputStream(in);
                try {
                    LogManager.getLogManager().readConfiguration(bin);
                } finally {
                    if (bin != null) {
                        bin.close();
                    }
                }
                return;
            }
            URL url = getClass().getClassLoader().getResource(propertyFile);
            if (url != null) {
                InputStream in = url.openStream();
                try {
                    LogManager.getLogManager().readConfiguration(in);
                } finally {
                    if (in != null) {
                        in.close();
                    }
                }
                return;
            }
        } catch (Exception e) {
            throw new EnvironmentalException("Unable to configure the Sun Logging Framework.", e);
        }
        throw new EnvironmentalException("Unable to find property file [" + propertyFile + "] in either Property dir [" + propertyDirectory.getPath() + "] or classapth");
    }
