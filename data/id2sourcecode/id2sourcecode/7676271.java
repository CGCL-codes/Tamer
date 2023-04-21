    public Model getMetaOntologyModel() throws Exception {
        String fileName = get("lgdMetaOntologyUrl", String.class);
        Model result = ModelFactory.createDefaultModel();
        URL url = new URL(fileName);
        InputStream in = url.openStream();
        ModelUtil.read(result, in, "TTL");
        in.close();
        return result;
    }
