    private Composite createDiagramPage() {
        Composite pageComposite = diagram.create(getContainer());
        if (diagram.isType("GRID")) {
            ViewpointDiagram viewpointDiagram = (ViewpointDiagram) diagram;
            createGraphicalViewer(viewpointDiagram.getDiagramComposite());
            createToolBarActions(viewpointDiagram.getToolbar());
            createAdditionalActions();
        } else {
        }
        return pageComposite;
    }
