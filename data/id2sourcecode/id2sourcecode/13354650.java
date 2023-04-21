    @Override
    public Tree<DataRecord> getTree(String treeName) {
        URL url = urlRefs.get(treeName);
        Tree<DataRecord> tree = null;
        try {
            InputStream inStream = url.openStream();
            parseTreeML(inStream);
            inStream.close();
        } catch (IOException exc) {
            throw new RuntimeException("Cannot access stream for " + treeName + " at " + url, exc);
        } catch (SAXException exc) {
            throw new RuntimeException("Cannot parse stream for " + treeName + " at " + url, exc);
        }
        return tree;
    }
