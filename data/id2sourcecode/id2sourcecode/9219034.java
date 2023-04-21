    public void run() {
        TableStubEditPart ep = (TableStubEditPart) getSelectedObjects().get(0);
        GraphicalViewer viewer = (GraphicalViewer) getWorkbenchPart().getAdapter(GraphicalViewer.class);
        AddStubbedTablesOperation op = new AddStubbedTablesOperation((DTableStub) ep.getModel(), viewer);
        op.execute(this);
    }
