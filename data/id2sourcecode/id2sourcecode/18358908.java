    private Model fetchAndTransform(String uri) throws SAXException, IOException {
        logger.debug("Accessing <" + uri + ">");
        URL url = new URL(uri);
        SAXParser parser = LiveSync.createParser();
        INodeSerializer nodeSerializer = new VirtuosoOseNodeSerializer();
        ITransformer<Entity, Model> entityTransformer = new OSMEntityToRDFTransformer(tagMapper, vocab, nodeSerializer);
        OsmEntityToRdfTransformer workflow = new OsmEntityToRdfTransformer(entityTransformer);
        parser.parse(url.openStream(), new OsmHandler(workflow, true));
        Model tmp = workflow.getModel();
        return postTransformer.transform(tmp);
    }
