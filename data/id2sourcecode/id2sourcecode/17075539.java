    private void setSelectedElements() {
        List selectedEditParts = this.editor.getDiagramGraphicalViewer().getSelectedEditParts();
        getSelectedElements().clear();
        for (Object selectedEditPart : selectedEditParts) {
            logger.info("currently selected: " + selectedEditPart);
            getSelectedElements().add((EObject) ((EditPart) selectedEditPart).getModel());
            logger.info("ROOT: " + ((EditPart) selectedEditPart).getRoot());
        }
    }
