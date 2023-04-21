    public PrintGraphicalViewerOperation(Printer p, GraphicalViewer g) {
        super(p);
        viewer = g;
        LayerManager lm = (LayerManager) viewer.getEditPartRegistry().get(LayerManager.ID);
        IFigure f = lm.getLayer(LayerConstants.PRINTABLE_LAYERS);
        setPrintSource(f);
    }
