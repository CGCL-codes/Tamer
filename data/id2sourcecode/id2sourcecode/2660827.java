    public void selectModel(UiElementNode uiNodeModel) {
        GraphicalViewer viewer = getGraphicalViewer();
        viewer.getControl().forceFocus();
        Object editPart = viewer.getEditPartRegistry().get(uiNodeModel);
        if (editPart instanceof EditPart) {
            viewer.select((EditPart) editPart);
        }
    }
