    protected GraphicalViewer createViewer(Composite parent) {
        StatusLineValidationMessageHandler validationMessageHandler = new StatusLineValidationMessageHandler(editorSite);
        GraphicalViewer viewer = new ValidationEnabledGraphicalViewer(validationMessageHandler);
        viewer.createControl(parent);
        viewer.getControl().setBackground(ColorConstants.white);
        viewer.setRootEditPart(new ScalableFreeformRootEditPart());
        viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));
        viewer.addDropTargetListener(new DataEditDropTargetListener(viewer));
        viewer.setEditPartFactory(getEditPartFactory());
        ContextMenuProvider provider = getContextMenuProvider(viewer);
        viewer.setContextMenu(provider);
        return viewer;
    }
