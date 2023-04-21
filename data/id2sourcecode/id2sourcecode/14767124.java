    protected void createGraphicalViewer(Composite parent) {
        logger.debug("Cr�ation du graphical viewer.");
        StatusLineValidationMessageHandler validationMessageHandler = new StatusLineValidationMessageHandler(getEditorSite());
        GraphicalViewer viewer = new ValidationEnabledGraphicalViewer(validationMessageHandler);
        viewer.createControl(parent);
        setGraphicalViewer(viewer);
        configureGraphicalViewer();
        hookGraphicalViewer();
        initializeGraphicalViewer();
    }
