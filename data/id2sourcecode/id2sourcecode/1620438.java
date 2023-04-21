    private static void loadResources(String name, Properties p) {
        try {
            Enumeration enumeration = TGFileUtils.getResourceUrls(name);
            while (enumeration.hasMoreElements()) {
                URL url = (URL) enumeration.nextElement();
                Properties properties = new Properties();
                properties.load(url.openStream());
                p.putAll(properties);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
