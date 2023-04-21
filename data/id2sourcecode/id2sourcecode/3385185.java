    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        doc.getStyle().setParentStyle(new CanvasStyle(getGraphicalViewer().getControl()));
        getEditDomain().setDefaultTool(new TextTool(styleService));
        getEditDomain().loadDefaultTool();
        getGraphicalViewer().setRootEditPart(new ScalableRootEditPart());
        ((FigureCanvas) getGraphicalViewer().getControl()).getViewport().setContentsTracksWidth(true);
        getGraphicalViewer().setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.MOD1), MouseWheelZoomHandler.SINGLETON);
    }
