    public EntityResolverImpl() throws Exception {
        URL url = new URL("http://linkedgeodata.org/vocabulary/core");
        InputStream in = null;
        try {
            in = url.openStream();
            Model model = ModelFactory.createDefaultModel();
            model.read(in, "", "N-TRIPLE");
            process(model);
        } finally {
            if (in != null) in.close();
        }
    }
