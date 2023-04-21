    private GraphicalViewer configureViewer() {
        GraphicalViewer viewer = getGraphicalViewer();
        viewer.setEditPartFactory(new ClassesEditPartFactory());
        viewer.setRootEditPart(new ScalableFreeformRootEditPart());
        return viewer;
    }
