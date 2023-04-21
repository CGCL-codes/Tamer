    public void createCatalinaBase(String pathReadBase, String pathWriteBase) {
        if (pathReadBase.equalsIgnoreCase(pathWriteBase)) {
            log.warn("CatalinaBase (writebase) was not written because it is the same as readbase");
            return;
        }
        String path = null;
        File file = null;
        FileWriter writer = null;
        file = new File(pathWriteBase + slash + "temp");
        if (!file.exists()) file.mkdir();
        file = new File(pathWriteBase + slash + "logs");
        if (!file.exists()) file.mkdir();
        file = new File(pathWriteBase + slash + "temp");
        if (!file.exists()) file.mkdir();
        file = new File(pathWriteBase + slash + "webapps");
        if (!file.exists()) file.mkdir();
        file = new File(pathWriteBase + slash + "work");
        if (!file.exists()) file.mkdir();
        path = pathWriteBase + slash + "config.properties";
        file = new File(path);
        if (!file.exists() || !config.isLockconfig()) {
            try {
                writer = new FileWriter(file);
                writer.write(PropertyReader.configToString(config, true));
                writer.close();
            } catch (IOException ioe) {
                System.out.println("Failed to create " + path);
                ioe.printStackTrace();
            }
        }
        File catalinaLocalhost = new File(pathWriteBase + slash + "conf" + slash + "Catalina" + slash + "localhost");
        System.out.println("catalinaLocalhost.exists(): " + catalinaLocalhost.exists());
        System.out.println("config.isLockconfig(): " + config.isLockconfig());
        if (catalinaLocalhost.exists() && !config.isLockconfig()) {
            File[] contexts = catalinaLocalhost.listFiles();
            System.out.println("contexts.length: " + contexts.length);
            for (int i = 0; i < contexts.length; i++) {
                System.out.println("for: " + i + " deleting: " + contexts[i].getAbsolutePath());
                contexts[i].delete();
            }
        }
        recurisiveCopy(new File(pathReadBase + slash + "conf"), new File(pathWriteBase + slash + "conf"));
    }
