    public synchronized void init(URI params) throws Exception {
        this.clearTransitions();
        initByResource = params;
        InputStream stream = null;
        if (params.getScheme().equalsIgnoreCase("file")) {
            stream = new FileInputStream(new File(params));
        } else {
            URL url = initByResource.toURL();
            stream = url.openStream();
        }
        try {
            unmarshal(stream);
        } finally {
            stream.close();
        }
    }
