    private GraphicalViewer getViewer() {
        IWorkbenchPart part = getWorkbenchPart();
        GraphicalViewer viewer = (GraphicalViewer) part.getAdapter(GraphicalViewer.class);
        if (viewer == null) {
            throw new RuntimeException("viewer cannot be null");
        }
        return viewer;
    }
