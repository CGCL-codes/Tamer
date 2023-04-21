    @Override
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        GraphicalViewer viewer = getGraphicalViewer();
        viewer.setEditPartFactory(new UiElementsEditPartFactory(mParent.getDisplay(), this));
        viewer.setRootEditPart(new ScalableFreeformRootEditPart());
        viewer.addDropTargetListener(new DropListener(viewer));
    }
