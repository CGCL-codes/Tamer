    public void setDiagram(ViewDiagram diagram) {
        this.diagram = diagram;
        GraphicalViewer gv = getGraphicalViewer();
        gv.setContents(diagram);
        bmViewer.refreshInput();
        markDirty();
    }
