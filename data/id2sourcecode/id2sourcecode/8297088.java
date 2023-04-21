    public void selectEditPartForModel(Object model) {
        EditPart editPart = (EditPart) getGraphicalViewer().getEditPartRegistry().get(model);
        if (editPart != null) getGraphicalViewer().appendSelection(editPart);
    }
