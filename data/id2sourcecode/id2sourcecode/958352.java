    public ClientSearchEngine(URL base, Hashtable params) {
        super(base, params);
        URL url;
        URLConnection uc;
        DataInputStream from;
        debug("Loading Search Database");
        try {
            String urldata = (String) params.get("data");
            debug("base=" + base.toExternalForm());
            debug("urldata=" + urldata);
            url = new URL(base, urldata + ".inv");
            debug("url: " + url);
            uc = url.openConnection();
            uc.setAllowUserInteraction(true);
            from = new DataInputStream(new BufferedInputStream(uc.getInputStream()));
            wordVec = new WordVector(from);
            url = new URL(base, urldata + ".dat");
            debug("url: " + url);
            uc = url.openConnection();
            uc.setAllowUserInteraction(true);
            from = new DataInputStream(new BufferedInputStream(uc.getInputStream()));
            docVec = new DocVector(from);
            debug("Search Database loaded");
        } catch (Exception e) {
            wordVec = null;
            docVec = null;
            debug("Failed to load Search DataBase");
            e.printStackTrace();
        }
    }
