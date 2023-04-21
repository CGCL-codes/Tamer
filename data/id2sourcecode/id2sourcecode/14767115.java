    public StatesMachinesDiagramPart getStatesMachinesEditPart() {
        GraphicalViewer viewer = getGraphicalViewer();
        if (viewer == null) return null;
        RootEditPart rootEditPart = getGraphicalViewer().getRootEditPart();
        if (rootEditPart == null) return null;
        return (StatesMachinesDiagramPart) rootEditPart.getChildren().get(0);
    }
