    private GraphicalViewer createGraphicalViewer(final Composite parent) {
        final GraphicalViewer graphicalViewer = new ScrollingGraphicalViewer();
        graphicalViewer.setEditPartFactory(new BigraphEditPartFactory());
        graphicalViewer.setRootEditPart(new ScalableRootEditPart());
        graphicalViewer.createControl(parent);
        graphicalViewer.getControl().setBackground(ColorConstants.listBackground);
        return graphicalViewer;
    }
