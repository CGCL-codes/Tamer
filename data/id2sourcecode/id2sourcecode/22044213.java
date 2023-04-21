    private AWSManager() {
        try {
            ClassLoader loader = AWSDataTransfer.class.getClassLoader();
            if (loader == null) loader = ClassLoader.getSystemClassLoader();
            String propFile = "AwsCredentials.properties";
            java.net.URL url = loader.getResource(propFile);
            props = new PropertiesCredentials(url.openStream());
        } catch (Exception e) {
            log.error("ERROR in loading AWS credentials.");
            e.printStackTrace();
        }
    }
