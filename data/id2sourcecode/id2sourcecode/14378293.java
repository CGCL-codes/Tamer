    private double placeTree(Tree<N, E> graph, N node, N precNode, int depth, int sumColumns) {
        Iterator<E> itEdges = graph.edgesIterator(node);
        double posNode = this.maxColumns;
        double lastPos = -1;
        double firstPos = -1;
        while (itEdges.hasNext() && !this.stopAsked()) {
            N oneNode = graph.getOpposite(itEdges.next(), node);
            if (!oneNode.equals(precNode)) {
                lastPos = this.placeTree(graph, oneNode, node, depth + 1, sumColumns);
                if (firstPos == -1) {
                    firstPos = lastPos;
                }
            }
        }
        if (graph.degree(node) >= 2 || precNode.equals(node)) {
            posNode = (firstPos + lastPos) / 2;
        } else {
            this.maxColumns++;
        }
        if (depth > this.maxDepth) {
            this.maxDepth = depth;
        }
        this.getPos(node).setLocation((posNode + sumColumns) * this.nodeSize, depth * this.nodeSize);
        this.nbNodesPassed++;
        this.setAdvancement(100.0 * this.nbNodesPassed / graph.nbNodes());
        return posNode;
    }
