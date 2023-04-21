    public static List rdf(String inputFile, Reasoner reasoner, Object subject, Object predicate, Object object) throws Exception {
        String reasonerName = "";
        if (reasoner != null) reasonerName = reasoner.getClass().getName();
        Model jenaModel = getCachedModel(inputFile + reasonerName);
        if (jenaModel == null) {
            Model defaultModel = ModelFactory.createDefaultModel();
            InputStream in;
            try {
                in = Thread.currentThread().getContextClassLoader().getResourceAsStream(inputFile);
                if (in == null) {
                    in = new FileInputStream(inputFile);
                }
            } catch (Exception e) {
                URL url = new URL(inputFile);
                in = url.openStream();
            }
            defaultModel.read(in, null);
            if (reasoner != null) jenaModel = ModelFactory.createInfModel(reasoner, defaultModel); else jenaModel = defaultModel;
            updateCache(jenaModel, inputFile + reasonerName);
        }
        return rdf(jenaModel, subject, predicate, object);
    }
