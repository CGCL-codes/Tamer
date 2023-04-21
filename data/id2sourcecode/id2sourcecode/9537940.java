    private void focus(Object object) {
        EditPart editPart = (EditPart) this.editor.getGraphicalViewer().getEditPartRegistry().get(object);
        if (editPart != null) {
            this.editor.getGraphicalViewer().select(editPart);
            this.editor.getGraphicalViewer().reveal(editPart);
        }
    }
