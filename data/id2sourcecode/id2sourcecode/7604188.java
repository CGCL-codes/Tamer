    protected void createGraphicalViewer(Composite parent) {
        GraphicalViewer viewer = new ScrollingGraphicalViewer() {

            protected LightweightSystem createLightweightSystem() {
                LightweightSystem lws = super.createLightweightSystem();
                lws.setUpdateManager(new CautiousUpdateManager());
                return lws;
            }
        };
        viewer.createControl(parent);
        setGraphicalViewer(viewer);
        configureGraphicalViewer();
        hookGraphicalViewer();
        initializeGraphicalViewer();
    }
