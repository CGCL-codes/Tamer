    @Override
    protected void createRootEditPart(GraphicalViewer viewer) {
        RootEditPart rootPart = new ScalableFreeformRootEditPart() {

            @Override
            protected ScalableFreeformLayeredPane createScaledLayers() {
                fScalableFreeformLayeredPane = super.createScaledLayers();
                showImagePane();
                return fScalableFreeformLayeredPane;
            }

            @SuppressWarnings("rawtypes")
            @Override
            public Object getAdapter(Class adapter) {
                if (adapter == AutoexposeHelper.class) {
                    return new ExtendedViewportAutoexposeHelper(this, new Insets(50), false);
                }
                return super.getAdapter(adapter);
            }
        };
        viewer.setRootEditPart(rootPart);
    }
