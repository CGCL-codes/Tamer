    public void automaticallyLayout(int direction) {
        GraphEditPart doc = (GraphEditPart) getGraphicalViewer().getRootEditPart().getContents();
        doc.automaticallyLayoutGraphs(direction);
    }
