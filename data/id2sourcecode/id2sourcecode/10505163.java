    public void setViewer(EditPartViewer viewer) {
        if (viewer == getCurrentViewer()) {
            return;
        }
        super.setViewer(viewer);
        if (viewer instanceof GraphicalViewer) {
            setDefaultCursor(Cursors.CROSS);
        } else {
            setDefaultCursor(Cursors.NO);
        }
    }
