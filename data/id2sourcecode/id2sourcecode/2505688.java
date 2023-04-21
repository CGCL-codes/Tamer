    private void executeCommand(Command command) {
        ERDiagramMultiPageEditor multiPageEditor = (ERDiagramMultiPageEditor) this.workbenchPage.getActiveEditor();
        ERDiagramEditor editor = (ERDiagramEditor) multiPageEditor.getActiveEditor();
        editor.getGraphicalViewer().getEditDomain().getCommandStack().execute(command);
    }
