    public HashMap processGraph(Network rGraph) {
        HashMap result = new HashMap();
        createCGDGraph(rGraph);
        graph.compute();
        transferFromCGDGraphToOrig();
        return result;
    }
