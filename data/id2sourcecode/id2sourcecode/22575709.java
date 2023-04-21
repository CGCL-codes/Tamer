    @Override
    public void createPartControl(Composite parent) {
        DiagramVisualizerSettings settings = new DiagramVisualizerSettings();
        settings.setEditPartFactory(new BurndownEditPartFactory());
        settings.setModelProvider(myModelProvider);
        settings.setLayoutPerformer(new BurndownLayoutPerformer());
        ScalableRootEditPart rootEditPart = new AntialiasedScalableRootEditPart();
        ConnectionLayer connectionLayer = (ConnectionLayer) rootEditPart.getLayer(LayerConstants.CONNECTION_LAYER);
        connectionLayer.setConnectionRouter(new PointsBasedConnectionRouter());
        settings.setRootEditPart(rootEditPart);
        myVisualizer = new DiagramVisualizer(settings, parent);
        myPrintAction.setGraphicalViewer(myVisualizer.getViewer());
        myVisualizer.getControl().addControlListener(myResizeListener);
        refresh();
    }
