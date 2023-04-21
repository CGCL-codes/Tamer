    public static Rectangle getDiagramExtents(GraphicalViewer diagramViewer) {
        FreeformGraphicalRootEditPart rootEditPart = (FreeformGraphicalRootEditPart) diagramViewer.getRootEditPart();
        if (!hasChildFigures(rootEditPart)) {
            return new Rectangle(100, 100, 100, 100);
        }
        IFigure figure = rootEditPart.getLayer(LayerConstants.PRINTABLE_LAYERS);
        Rectangle extents = figure.getBounds().getCopy();
        IFigure primaryLayer = rootEditPart.getLayer(LayerConstants.PRIMARY_LAYER);
        Rectangle rect = new Rectangle(primaryLayer.getBounds().width, primaryLayer.getBounds().height, primaryLayer.getBounds().x, primaryLayer.getBounds().y);
        for (Object child : primaryLayer.getChildren()) {
            if (child instanceof FreeformLayer) {
                for (Object o : ((Figure) child).getChildren()) {
                    getDiagramExtents((IFigure) o, rect);
                }
            }
        }
        IFigure connectionLayer = rootEditPart.getLayer(LayerConstants.CONNECTION_LAYER);
        Rectangle rect2 = new Rectangle(connectionLayer.getBounds().width, connectionLayer.getBounds().height, connectionLayer.getBounds().x, connectionLayer.getBounds().y);
        for (Object child : connectionLayer.getChildren()) {
            if (child instanceof IFigure) {
                getDiagramExtents((IFigure) child, rect2);
            }
        }
        rect.x = Math.min(rect.x, rect2.x);
        rect.y = Math.min(rect.y, rect2.y);
        rect.width = Math.max(rect.width, rect2.width);
        rect.height = Math.max(rect.height, rect2.height);
        extents.x = Math.max(extents.x, rect.x);
        extents.y = Math.max(extents.y, rect.y);
        extents.width = Math.min(extents.width, rect.width - extents.x);
        extents.height = Math.min(extents.height, rect.height - extents.y);
        int BORDER_WIDTH = 10;
        extents.x -= BORDER_WIDTH;
        extents.y -= BORDER_WIDTH;
        extents.width += BORDER_WIDTH * 2;
        extents.height += BORDER_WIDTH * 2;
        return extents;
    }
