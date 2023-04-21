    protected void initVOClassesWithViews() {
        File dir = new File(System.getProperty("user.dir") + "/virtualObjects");
        FilenameFilter filter = new FilenameFilter() {

            public boolean accept(File file, String name) {
                return name.endsWith("jar");
            }
        };
        File[] files = dir.listFiles(filter);
        voClassesWithViews = new Vector<Vector<String>>();
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
                Vector<String> vo = new Vector<String>();
                vo.addElement(conn.getManifest().getMainAttributes().getValue("VO-Name"));
                String supportedViews = conn.getManifest().getMainAttributes().getValue("Supported-Views");
                String[] viewNames = supportedViews.split(",");
                for (String viewName : viewNames) vo.addElement(viewName.trim());
                voClassesWithViews.addElement(vo);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
