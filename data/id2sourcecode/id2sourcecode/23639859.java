    public void run() {
        GraphicalViewer viewer = (GraphicalViewer) part.getAdapter(GraphicalViewer.class);
        if (viewer != null) viewer.setSelection(new StructuredSelection(viewer.getContents().getChildren()));
    }
