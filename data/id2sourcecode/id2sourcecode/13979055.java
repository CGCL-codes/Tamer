    protected void createDiagramAction(GraphicalViewer viewer) {
        returnMessageAction = new AddReturnMessageAction(getCommandStack(), viewer);
        importClassModelAction = new ImportClassModelAction(getCommandStack(), viewer);
    }
