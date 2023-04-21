    @Override
    protected void setGraphicalViewer(GraphicalViewer viewer) {
        super.setGraphicalViewer(viewer);
        viewer.setContextMenu(createContextMenu(viewer));
    }
