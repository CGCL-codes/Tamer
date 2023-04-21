    void loadGraph(URL url) {
        try {
            URLConnection c = url.openConnection();
            InputStream is = new BufferedInputStream(c.getInputStream());
            GraphMLFile f = new GraphMLFile();
            graph = f.load(new InputStreamReader(is));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
