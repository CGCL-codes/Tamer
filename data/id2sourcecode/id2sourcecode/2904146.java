    private static String getAdjacencyList(final ChebiTerm chebiTerm) throws IOException {
        final String smiles = chebiTerm.getSmiles();
        if (smiles == null) {
            return null;
        }
        final URL url = new URL("http://rmg.mit.edu/adjacencylist/" + smiles);
        return new String(StreamReader.read(url.openStream()));
    }
