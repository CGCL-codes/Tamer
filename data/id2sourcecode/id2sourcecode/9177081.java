    @Override
    protected void initializeGraphicalViewer() {
        super.initializeGraphicalViewer();
        getGraphicalViewer().setContents(process);
        getGraphicalViewer().addDropTargetListener(new GEFEditorDropTargetListener(this));
    }
