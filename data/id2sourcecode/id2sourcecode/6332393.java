    private DiagramEditPart getDiagramEditPart() {
        if (getGraphicalViewer() != null) {
            for (Object obj : getGraphicalViewer().getRootEditPart().getChildren()) {
                if (obj instanceof DiagramEditPart) {
                    return (DiagramEditPart) obj;
                }
            }
        }
        return null;
    }
