    @Override
    public DiagramEditPart getDiagramEditPart() {
        if (getDiagramGraphicalViewer() == null) return null;
        return super.getDiagramEditPart();
    }
