    @Override
    public void selectElements(IArchimateElement[] elements) {
        List<EditPart> editParts = new ArrayList<EditPart>();
        for (IArchimateElement element : elements) {
            for (IDiagramModelComponent dc : DiagramModelUtils.findDiagramModelComponentsForElement(getModel(), element)) {
                EditPart editPart = (EditPart) getGraphicalViewer().getEditPartRegistry().get(dc);
                if (editPart != null && editPart.isSelectable() && !editParts.contains(editPart)) {
                    editParts.add(editPart);
                }
            }
            if (ConnectionPreferences.useNestedConnections() && element instanceof IRelationship) {
                for (IDiagramModelArchimateObject[] list : DiagramModelUtils.findNestedComponentsForRelationship(getModel(), (IRelationship) element)) {
                    EditPart editPart1 = (EditPart) getGraphicalViewer().getEditPartRegistry().get(list[0]);
                    EditPart editPart2 = (EditPart) getGraphicalViewer().getEditPartRegistry().get(list[1]);
                    if (editPart1 != null && editPart1.isSelectable() && !editParts.contains(editPart1)) {
                        editParts.add(editPart1);
                    }
                    if (editPart2 != null && editPart2.isSelectable() && !editParts.contains(editPart2)) {
                        editParts.add(editPart2);
                    }
                }
            }
        }
        if (!editParts.isEmpty()) {
            getGraphicalViewer().setSelection(new StructuredSelection(editParts));
            getGraphicalViewer().reveal(editParts.get(0));
        } else {
            getGraphicalViewer().setSelection(StructuredSelection.EMPTY);
        }
    }
