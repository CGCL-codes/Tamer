    public void run() {
        GraphicalViewer graphicalViewer = editor.getGraphicalViewer();
        ERDiagramEditPart diagramEditPart = (ERDiagramEditPart) graphicalViewer.getContents();
        ERDiagram diagram = (ERDiagram) diagramEditPart.getModel();
        Object[] elements = diagram.getTableNodes().toArray();
        if (elements.length == 0) {
            MessageDialog.openWarning(null, "Warning", "No elements");
            return;
        }
        ElementListSelectionDialog dialog = new ElementListSelectionDialog(null, new LabelProvider() {

            public String getText(Object obj) {
                return ((ERTableNode) obj).getName();
            }
        });
        dialog.setTitle("Select table title");
        dialog.setMessage("Select table message");
        dialog.setElements(elements);
        dialog.open();
        Object aobj[] = dialog.getResult();
        if (aobj == null) {
            return;
        }
        EditPart editPart = (EditPart) graphicalViewer.getEditPartRegistry().get(aobj[0]);
        graphicalViewer.reveal(editPart);
        graphicalViewer.select(editPart);
    }
