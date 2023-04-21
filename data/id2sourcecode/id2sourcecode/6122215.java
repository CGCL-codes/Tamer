        return graphPath;
    }

    public void setGraphPath(Vector graphPath) {
        this.graphPath = graphPath;
    }

    private void positionNode(TNNode node, int x, int y) {
        positionNode(node, x, y, graphics.getNodeBoundsX(), graphics.getNodeBoundsY());
        refreshGraph();
    }

    private void positionNode(TNNode node, int x, int y, int width, int height) {
        int real_width = Math.min(width, node.getLabelWidth());
