    private ScalableFreeformRootEditPart getRootEditPart() {
        GraphicalViewer graphicalViewer = ((ClassDiagramEditor) getWorkbenchPart()).getViewer();
        ScalableFreeformRootEditPart rootEditPart = (ScalableFreeformRootEditPart) graphicalViewer.getRootEditPart();
        return rootEditPart;
    }
