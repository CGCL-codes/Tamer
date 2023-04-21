    protected void findAvailableViews() {
        File dir = new File(System.getProperty("user.dir") + "/views");
        File[] files = dir.listFiles();
        FilenameFilter filter = new FilenameFilter() {

            public boolean accept(File file, String name) {
                return name.endsWith("jar");
            }
        };
        files = dir.listFiles(filter);
        views = new HashMap<String, String>();
        URL[] urls = new URL[files.length];
        for (int i = 0; i < urls.length; i++) {
            try {
                urls[i] = new URL("jar:file:" + files[i] + "!/");
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
        }
        loader.addURLs(urls);
        for (URL url : urls) {
            try {
                JarURLConnection conn = (JarURLConnection) url.openConnection();
                String name = conn.getManifest().getMainAttributes().getValue("View-Name");
                String shortName = conn.getManifest().getMainAttributes().getValue("SPOTWorld-Name");
                views.put(shortName, name);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
