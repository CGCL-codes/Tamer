    private void configureContextMenu(final GraphicalViewer viewer) {
        final ContextMenuProvider cmProvider = new VGAP4MapViewerContextMenuProvider(viewer, this.getActionRegistry());
        ((VGAP4MapViewerContextMenuProvider) cmProvider).registerAction(new ImportTurnDataAction(this));
        ((VGAP4MapViewerContextMenuProvider) cmProvider).registerAction(new WorkbenchPartAction(this) {

            @Override
            public boolean isEnabled() {
                return true;
            }

            @Override
            public void run() {
                super.run();
                this.execute(new ZoomPlusDataCommand("Zoom +", MainMapPage.this.getMapModel()));
            }

            @Override
            protected boolean calculateEnabled() {
                return true;
            }

            @Override
            protected void init() {
                super.init();
                this.setId("ZoomPlus");
                this.setDescription("Make the Map bigger with positive zoom.");
                this.setEnabled(true);
                this.setLazyEnablementCalculation(false);
                this.setImageDescriptor(Activator.getImageDescriptor("icons/zoomplus_on.gif"));
                this.setDisabledImageDescriptor(Activator.getImageDescriptor("icons/zoomplus.gif"));
                this.setText("Zoom +");
                this.setToolTipText("Make zoom to Map.");
            }
        });
        ((VGAP4MapViewerContextMenuProvider) cmProvider).registerAction(new WorkbenchPartAction(this) {

            @Override
            public boolean isEnabled() {
                return true;
            }

            @Override
            public void run() {
                super.run();
                this.execute(new ZoomMinusDataCommand("Zoom -", MainMapPage.this.getMapModel()));
            }

            @Override
            protected boolean calculateEnabled() {
                return true;
            }

            @Override
            protected void init() {
                super.init();
                this.setId("ZoomMinus");
                this.setDescription("Make the Map smaller with positive zoom.");
                this.setEnabled(true);
                this.setLazyEnablementCalculation(false);
                this.setImageDescriptor(Activator.getImageDescriptor("icons/zoomminus_on.gif"));
                this.setDisabledImageDescriptor(Activator.getImageDescriptor("icons/zoomminus.gif"));
                this.setText("Zoom -");
                this.setToolTipText("Reduce zoom to Map.");
            }
        });
        ((VGAP4MapViewerContextMenuProvider) cmProvider).registerAction(new WorkbenchPartAction(this) {

            @Override
            public boolean isEnabled() {
                return true;
            }

            @Override
            public void run() {
                super.run();
                this.execute(new ZoomMinusDataCommand("Zoom 	Reset", MainMapPage.this.getMapModel()));
            }

            @Override
            protected boolean calculateEnabled() {
                return true;
            }

            @Override
            protected void init() {
                super.init();
                this.setId(VGAP4MapViewerContextMenuProvider.ZOOMRESET_ACTION);
                this.setDescription("Reset the zoom to the 100% factor.");
                this.setEnabled(true);
                this.setLazyEnablementCalculation(false);
                this.setImageDescriptor(Activator.getImageDescriptor("icons/zoom_on.gif"));
                this.setDisabledImageDescriptor(Activator.getImageDescriptor("icons/zoom.gif"));
                this.setText("Zoom Reset");
                this.setToolTipText("Reset the zoom to the 100% factor.");
            }
        });
        viewer.setContextMenu(cmProvider);
        this.getSite().registerContextMenu(cmProvider, viewer);
    }
