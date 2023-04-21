    @Override
    protected void createGraphicalViewer(Composite parent) {
        IEditorSite editorSite = getEditorSite();
        StatusLineValidationMessageHandler validationMessageHandler = new StatusLineValidationMessageHandler(editorSite);
        GraphicalViewer viewer = new ValidationEnabledGraphicalViewer(validationMessageHandler);
        viewer.createControl(parent);
        viewer.getControl().setBackground(ColorConstants.white);
        viewer.setRootEditPart(new ScalableFreeformRootEditPart());
        viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));
        viewer.addDropTargetListener(new DataEditDropTargetListener(viewer, diagram));
        viewer.setEditPartFactory(new DiagramEditPartFactory());
        getSite().setSelectionProvider(viewer);
        getEditDomain().addViewer(viewer);
        viewer.setContents(diagram);
        this.graphicalViewer = viewer;
    }
