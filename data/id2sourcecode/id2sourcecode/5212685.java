    @Override
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        final GraphicalViewer viewer = this.getGraphicalViewer();
        viewer.setRootEditPart(new ScalableRootEditPart());
        viewer.setEditPartFactory(new FigureDeveloperEditPartFactory(new FigureDeveloperFigureFactory()));
        viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));
    }
