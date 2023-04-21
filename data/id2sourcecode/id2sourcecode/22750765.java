    public GraphViewer getGraphicalViewer(Composite parent) {
        if (graphicalViewer == null) {
            graphicalViewer = new GraphViewer(parent, ZestStyles.NONE);
        }
        return graphicalViewer;
    }
