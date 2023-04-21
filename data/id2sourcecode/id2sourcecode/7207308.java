    public static synchronized InputStream openStream(String filename, Class loader, URLResourceInfo info, boolean cache) {
        URL url;
        URLConnection c = null;
        if (info == null) info = new URLResourceInfo();
        if (loader == null) loader = IOManager.class;
        try {
            url = new URL(filename);
            c = url.openConnection();
            c.setUseCaches(cache);
        } catch (Exception e) {
            Log.log(IOManager.class, e);
            try {
                url = loader.getResource(filename);
                c = url.openConnection();
                c.setUseCaches(cache);
            } catch (Exception e2) {
                Log.log(IOManager.class, e2);
                return null;
            }
        }
        getResourceInfo(c, info);
        try {
            InputStream i = c.getInputStream();
            return i;
        } catch (IOException e) {
            Log.log(IOManager.class, e);
            return null;
        }
    }
