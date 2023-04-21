    protected void initializeGraphicalViewer() {
        getGraphicalViewer().setContents(dashboard);
        updateRulers();
        getGraphicalViewer().addDropTargetListener(new VariableDropTargetListener(getGraphicalViewer()));
        getGraphicalViewer().addDropTargetListener(new FileImageDropTargetListener(this, getGraphicalViewer()));
    }
