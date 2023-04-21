    private EditPart searchEditPart(EObject obj) {
        for (Object object : editor.getDiagramGraphicalViewer().getEditPartRegistry().values()) {
            EditPart editPart = (EditPart) object;
            if (getEditPartElement(editPart) == obj) {
                while (getEditPartElement(editPart.getParent()) == getEditPartElement(editPart)) {
                    editPart = editPart.getParent();
                }
                return editPart;
            }
        }
        return null;
    }
