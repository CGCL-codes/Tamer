    private void disposeRulerViewer(GraphicalViewer viewer) {
        if (viewer == null) return;
        RangeModel rModel = new DefaultRangeModel();
        Viewport port = ((FigureCanvas) viewer.getControl()).getViewport();
        port.setHorizontalRangeModel(rModel);
        port.setVerticalRangeModel(rModel);
        rulerEditDomain.removeViewer(viewer);
        viewer.getControl().dispose();
    }
