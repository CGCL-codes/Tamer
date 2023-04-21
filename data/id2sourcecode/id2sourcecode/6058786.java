    private void addEdge(double[] Rs, double[][] RnormalizedWeights, Graph<ItemPredicate> graph, Map<ItemPredicate, Node<ItemPredicate>> nodeMap, ItemPredicate cause, ItemPredicate caused, Explanation nullModel, PerspectiveObserver redrawer, boolean debug) {
        Node<ItemPredicate> posNode = ensureNode(Rs, graph, nodeMap, caused, redrawer);
        Node<ItemPredicate> negNode = ensureNode(Rs, graph, nodeMap, cause, redrawer);
        Edge<ItemPredicate> edge = graph.getEdge(posNode, negNode);
        if (edge == null) edge = graph.addEdge((String) null, posNode, negNode);
        int causeIndex = facetIndex(cause);
        int causedIndex = facetIndex(caused);
        double forwardWeight = RnormalizedWeights[causeIndex][causedIndex];
        if (debug) {
            String label = formatWeight(forwardWeight);
            if (nullModel.facets().contains(cause) && nullModel.facets().contains(caused)) label = formatWeight(nullModel.getRNormalizedWeight(cause, caused)) + " > " + label;
            edge.setLabel("        " + label + "        ", posNode);
            edge.setLabel(formatWeight(getWeight(cause, caused)) + " (" + formatWeight(effectiveWeight(cause, caused)) + ")", Edge.CENTER_LABEL);
        } else {
            double backwardWeight = RnormalizedWeights[causedIndex][causeIndex];
            double averageWeight = (forwardWeight + backwardWeight) / 2;
            edge.setLabel(formatWeight(averageWeight), Edge.CENTER_LABEL);
        }
    }
