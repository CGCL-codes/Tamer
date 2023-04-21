    private Command createCommand() {
        if (!calculateEnabled()) {
            return null;
        }
        NodeSet pasteList = CopyManager.paste();
        int numberOfCopy = CopyManager.getNumberOfCopy();
        boolean first = true;
        int x = 0;
        int y = 0;
        for (NodeElement nodeElement : pasteList) {
            if (first || x > nodeElement.getX()) {
                x = nodeElement.getX();
            }
            if (first || y > nodeElement.getY()) {
                y = nodeElement.getY();
            }
            first = false;
        }
        EditPart editPart = this.editor.getGraphicalViewer().getContents();
        ERDiagram diagram = (ERDiagram) editPart.getModel();
        Command command = new PasteCommand(editor, pasteList, diagram.mousePoint.x - x + (numberOfCopy - 1) * 20, diagram.mousePoint.y - y + (numberOfCopy - 1) * 20);
        return command;
    }
