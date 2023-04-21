    @Override
    public void run() {
        IWorkbenchPart part = getWorkbenchPart();
        if (part != null) {
            GraphicalViewer viewer = (GraphicalViewer) part.getAdapter(GraphicalViewer.class);
            if (viewer != null) {
                LayerManager layerManager = (LayerManager) viewer.getEditPartRegistry().get(LayerManager.ID);
                IFigure contentLayer = layerManager.getLayer(LayerConstants.PRIMARY_LAYER);
                IFigure connectionLayer = layerManager.getLayer(LayerConstants.CONNECTION_LAYER);
                Dimension size = ((AbstractGraphicalEditPart) viewer.getRootEditPart().getContents()).getFigure().getPreferredSize();
                PrintableFigure figure = new PrintableFigure(contentLayer, connectionLayer, size);
                String diagramLabel = part.getTitle();
                PreviewDialog pd = new PreviewDialog(figure, diagramLabel, part.getSite().getShell());
                pd.open();
            }
        }
    }
