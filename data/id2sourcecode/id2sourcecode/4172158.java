    private ScalableFreeformRootEditPart getRootEditPart() {
        RootEditPart rep = myGraphicalViewer.getRootEditPart();
        if (rep instanceof ScalableFreeformRootEditPart) {
            return (ScalableFreeformRootEditPart) rep;
        }
        return null;
    }
