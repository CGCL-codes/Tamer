    public ConfigurationResolverEnvironmentVariable(final String key) {
        String fileName = System.getProperty(key);
        if (fileName == null) {
            exception = new ConfigurationException("no property for configuration file or URL was given as JVM argument. Please add the following to the java.exe startup command: -D" + key + "=<location of file>");
            return;
        }
        boolean isURL = false;
        try {
            new URL(fileName);
            isURL = true;
        } catch (Exception e) {
            isURL = false;
        }
        if (isURL) {
            String interpretedURLName = null;
            Properties tmpProperties = new Properties();
            try {
                URL url = new URL(fileName);
                interpretedURLName = url.toExternalForm();
                byte[] bytes = readEntireFileContents(url.openStream());
                InputStream is = new ByteArrayInputStream(bytes);
                PropertyInputStreamValidator.validate(is);
                is.reset();
                tmpProperties.load(is);
            } catch (Exception e) {
                exception = new ConfigurationException("environment property '" + key + "' had value '" + fileName + "', which I interpreted as url '" + interpretedURLName + "' but this can't be used to read the configuration from: " + e.getMessage(), e);
                return;
            }
            sourceDescription = "url " + interpretedURLName + " (from environment property '" + key + "')";
            properties = tmpProperties;
        } else {
            File file = new File(fileName);
            String interpretedFileName = null;
            Properties tmpProperties = new Properties();
            try {
                interpretedFileName = file.getCanonicalPath();
                FileInputStream is = new FileInputStream(file);
                PropertyInputStreamValidator.validate(is);
                is = new FileInputStream(file);
                tmpProperties.load(is);
            } catch (Exception e) {
                exception = new ConfigurationException("environment property '" + key + "' had value '" + fileName + "', which I interpreted as '" + interpretedFileName + "' but this can't be used to read the configuration from: " + e.getMessage(), e);
                return;
            }
            sourceDescription = "file " + interpretedFileName + " (from environment property '" + key + "')";
            properties = tmpProperties;
        }
    }
