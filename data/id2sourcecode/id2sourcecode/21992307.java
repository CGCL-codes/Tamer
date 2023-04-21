    public Rectangle getBounds(ScrollingGraphicalViewer viewer, Figure f) {
        CompoundModel rootModel = (CompoundModel) ((ChsRootEditPart) viewer.getRootEditPart().getChildren().get(0)).getModel();
        org.eclipse.draw2d.geometry.Rectangle bounds = rootModel.calculateBounds();
        org.eclipse.draw2d.geometry.Rectangle boundsRoot = f.getBounds();
        boundsRoot.setSize(bounds.x + bounds.width + CompoundModel.MARGIN_SIZE, bounds.y + bounds.height + CompoundModel.MARGIN_SIZE);
        return new Rectangle(boundsRoot.x, boundsRoot.y, boundsRoot.width, boundsRoot.height);
    }
