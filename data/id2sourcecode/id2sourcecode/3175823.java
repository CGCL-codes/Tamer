    private void execute(Command command) {
        ERDiagramEditor selectedEditor = (ERDiagramEditor) this.getActiveEditor();
        selectedEditor.getGraphicalViewer().getEditDomain().getCommandStack().execute(command);
    }
