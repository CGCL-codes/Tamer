    public void setInput(GraphicalViewer graphicalViewer) {
        myGraphicalViewer = graphicalViewer;
        ScalableFreeformRootEditPart root = getRootEditPart();
        if ((root != null) && (myThumbnail != null)) {
            myThumbnail.setViewport((Viewport) root.getFigure());
            myThumbnail.setSource(root.getLayer(LayerConstants.PRINTABLE_LAYERS));
        }
    }
