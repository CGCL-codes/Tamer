    private void read() throws IOException {
        Properties props = new Properties();
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = getClass().getClassLoader();
        }
        Enumeration<URL> modules = cl.getResources("META-INF/jmonit.properties");
        while (modules.hasMoreElements()) {
            URL url = (URL) modules.nextElement();
            props.load(url.openStream());
        }
        InputStream config = cl.getResourceAsStream("jmonit.properties");
        if (config != null) {
            props.load(config);
        }
    }
