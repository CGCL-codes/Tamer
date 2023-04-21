    @Override
    protected void initializeGraphicalViewer() {
        if (diagram.isType("GRID")) {
            GraphicalViewer viewer = getGraphicalViewer();
            if (viewer != null) {
                viewer.setContents(diagram);
            }
        }
    }
