    public final void execute() {
        openDialog();
        if (!enableCommand) return;
        getLearningDesignDataModel().getEditor().getGraphicalViewer().deselectAll();
        createNewRoles();
        createEnvironments();
        createNewActivities();
        createNewPoints();
        createInteractions();
        assignRoles();
        assignEnvironments();
        assignOperations();
        assignConnections();
        assignTextResources();
        super.execute();
    }
