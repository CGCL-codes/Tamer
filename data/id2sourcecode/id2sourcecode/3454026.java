    private void deleteEdge(int i) {
        int newLength = edges.length - 1;
        if (newLength == 0) {
            edges = null;
            return;
        }
        Edge[] edgesNew = new Edge[newLength];
        int j = 0;
        for (; j < i; ++j) edgesNew[j] = edges[j];
        for (; j < newLength; ++j) edgesNew[j] = edges[j + 1];
        edges = edgesNew;
    }
