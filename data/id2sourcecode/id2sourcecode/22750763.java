    @Override
    public void createPartControl(Composite parent) {
        graphicalViewer = getGraphicalViewer(parent);
        this.g = createZestView(parent);
    }
