    @Override
    protected void initializeGraphicalViewer() {
        super.initializeGraphicalViewer();
        final GraphicalViewer viewer = this.getGraphicalViewer();
        viewer.setContents(this.getMapModel());
        ((AbstractPropertyChanger) this.getMapModel()).addPropertyChangeListener(this);
        viewer.addDropTargetListener(this.createTransferDropTargetListener());
        this.getCommandStack().markSaveLocation();
    }
