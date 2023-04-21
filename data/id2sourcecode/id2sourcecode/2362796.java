    private Point getCursorLocation() {
        if (this.courserLocation != null) {
            return this.courserLocation.getCopy();
        }
        int x = 0;
        int y = 0;
        org.eclipse.swt.graphics.Point p = this.lddm.getEditor().getGEFEditor().getAbsolutPosition();
        org.eclipse.swt.graphics.Point pos = this.lddm.getEditor().getGraphicalViewer().getControl().getDisplay().getCursorLocation();
        x = pos.x - p.x;
        y = pos.y - p.y - 50;
        this.courserLocation = new Point(x, y);
        this.courserLocation = this.lddm.getEditor().getGEFEditor().translateLocationScrollbar(this.courserLocation);
        return this.courserLocation.getCopy();
    }
