    protected ERDiagram getDiagram() {
        EditPart editPart = this.editor.getGraphicalViewer().getContents();
        ERDiagram diagram = (ERDiagram) editPart.getModel();
        return diagram;
    }
