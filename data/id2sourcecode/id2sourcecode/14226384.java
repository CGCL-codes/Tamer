    private void loadJMockitPropertiesFilesFromClasspath() throws IOException {
        Enumeration<URL> allFiles = Thread.currentThread().getContextClassLoader().getResources("jmockit.properties");
        int numFiles = 0;
        while (allFiles.hasMoreElements()) {
            URL url = allFiles.nextElement();
            InputStream propertiesFile = url.openStream();
            if (numFiles == 0) {
                try {
                    config.load(propertiesFile);
                } finally {
                    propertiesFile.close();
                }
            } else {
                Properties properties = new Properties();
                try {
                    properties.load(propertiesFile);
                } finally {
                    propertiesFile.close();
                }
                addPropertyValues(properties);
            }
            numFiles++;
        }
    }
