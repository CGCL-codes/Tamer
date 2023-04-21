    @Override
    public Graph<DataRecord, DataRecord> getGraph(String graphName) {
        URL url = urlRefs.get(graphName);
        Graph<DataRecord, DataRecord> graph = null;
        try {
            InputStream inStream = url.openStream();
            graph = parseGraphML(inStream, graphName);
            inStream.close();
        } catch (IOException exc) {
            throw new RuntimeException("Cannot access stream for " + graphName + " at " + url, exc);
        } catch (SAXException exc) {
            throw new RuntimeException("Cannot parse stream for " + graphName + " at " + url, exc);
        }
        return graph;
    }
