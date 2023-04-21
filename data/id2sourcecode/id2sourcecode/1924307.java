    @Override
    protected void initializeGraphicalViewer() {
        GraphicalViewer viewer = getGraphicalViewer();
        if (graph == null) {
            viewer.setContents(status);
        } else {
            viewer.setContents(graph);
            if (!(Boolean) graph.getValue(Graph.PROPERTY_HAS_LAYOUT)) {
                automaticallyLayout(PositionConstants.EAST);
            }
        }
        this.tabbedPropertySheetPage = new TabbedPropertySheetPage(this);
    }
