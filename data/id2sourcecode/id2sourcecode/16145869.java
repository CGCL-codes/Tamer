    private static synchronized void init() {
        if (!isInit) {
            URL url = Version.class.getClassLoader().getResource(VERSION_FILE);
            if (url == null) {
                System.err.println("Could not locate " + VERSION_FILE + " in jsqsh.jar. Unable to determine version");
                return;
            }
            try {
                Properties props = new Properties();
                InputStream in = url.openStream();
                props.load(in);
                in.close();
                version = props.getProperty("build.version");
                buildDate = props.getProperty("build.date");
            } catch (Throwable e) {
                System.err.println("Failed to read " + VERSION_FILE + " in jsqsh.jar: " + e.getMessage() + ". Cannot determine version number");
            }
            isInit = true;
        }
    }
