    private TObjectIntHashMap<Vertex> generateDegreeSequence(Graph graph, int maxDegree) {
        TObjectIntHashMap<Vertex> kMap = new TObjectIntHashMap<Vertex>();
        for (Vertex vertex : graph.getVertices()) {
            boolean accept = false;
            while (!accept) {
                try {
                    int k = random.nextInt(maxDegree + 1);
                    double p = degreeDistribution.value(k);
                    if (p >= random.nextDouble()) {
                        kMap.put(vertex, k);
                        accept = true;
                    }
                } catch (FunctionEvaluationException e) {
                    e.printStackTrace();
                }
            }
        }
        return kMap;
    }
