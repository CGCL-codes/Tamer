    private Point getDockableLocation(Point size) {
        Control control = myGraphicalViewer.getControl();
        Rectangle controlBounds = control.getBounds();
        controlBounds = Geometry.toDisplay(control.getParent(), controlBounds);
        org.eclipse.draw2d.geometry.Rectangle viewportBounds = getRootEditPart().getFigure().getBounds();
        int x = (controlBounds.x + viewportBounds.width) - size.x;
        int y = (controlBounds.y + viewportBounds.height) - size.y;
        return new Point(x, y);
    }
