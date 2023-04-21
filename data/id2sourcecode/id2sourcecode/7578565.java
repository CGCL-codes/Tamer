    public void resetRootModel(final ProcessViewModel model) {
        lf.finest("Refresh process view model.", "ProcessViewEditor", "setInput");
        rootModel.removeAllChildren();
        rootModel = model;
        final GraphicalViewer viewer = getGraphicalViewer();
        viewer.setContents(rootModel);
    }
