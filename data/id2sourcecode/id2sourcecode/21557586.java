    private static void readConfiguration() {
        try {
            String configFile = System.getProperty(CONFIG_FILE_PROPERTY, CONFIG_FILE_DEFAULT);
            URL url = new URL(configFileContext, configFile);
            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            connection.connect();
            long lastModified = connection.getLastModified();
            if (lastModified > lastRead) {
                lastRead = lastModified;
                InputStream in = null;
                try {
                    in = new BufferedInputStream(connection.getInputStream());
                    LogManager.getLogManager().readConfiguration(in);
                    updateResampleInterval();
                    lastException = null;
                    logger.log(Level.INFO, "Read configuration {0}", configFile);
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                        }
                    }
                }
            }
        } catch (Exception e) {
            printException("Error reading logging configuration file: " + e);
        }
    }
