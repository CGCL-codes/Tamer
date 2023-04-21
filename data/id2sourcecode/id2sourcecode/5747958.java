    @Override
    public void createPartControl(final Composite parent) {
        final GraphicalViewer viewer = new ScrollingGraphicalViewer();
        viewer.createControl(parent);
        this.getEditDomain().addViewer(viewer);
        graphicalViewer = viewer;
        this.configureGraphicalViewer();
        {
            this.getSelectionSynchronizer().addViewer(this.getGraphicalViewer());
            this.getSite().setSelectionProvider(this.getGraphicalViewer());
        }
        this.initializeGraphicalViewer();
    }
