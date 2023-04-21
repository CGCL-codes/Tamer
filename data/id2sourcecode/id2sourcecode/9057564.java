    public synchronized void writeToFile(String[] tags) {
        Properties props = new Properties();
        try {
            URL url = Thread.currentThread().getContextClassLoader().getResource("cache.conf");
            if (url == null) {
                url = new URL("file:///C:/Program Files/Apache Software Foundation/Tomcat 5.5/webapps/wsrf/cache.conf");
            }
            props.load(url.openStream());
            if (props.containsKey("filename")) filename = props.getProperty("filename");
            writeToFile(filename, tags);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
