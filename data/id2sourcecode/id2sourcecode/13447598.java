    public void createPartControl(Composite parent) {
        graphicalViewer = new ScrollingGraphicalViewer();
        graphicalViewer.createControl(parent);
        ScalableRootEditPart root = new ScalableRootEditPart();
        graphicalViewer.setRootEditPart(root);
        graphicalViewer.setEditPartFactory(new VisualEditPartFactory());
        graphicalViewer.setEditDomain(new EditDomain());
        root.getFigure().setBackgroundColor(ColorConstants.white);
        drawGraph();
        makeActions();
        hookContextMenu();
        contributeToActionBars();
    }
