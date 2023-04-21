    public void run() {
        GraphicalViewer viewer = (GraphicalViewer) getWorkbenchPart().getAdapter(GraphicalViewer.class);
        LayoutOperation operation = new LayoutOperation(viewer);
        operation.execute(this);
    }
