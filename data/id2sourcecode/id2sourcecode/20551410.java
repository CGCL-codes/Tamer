    @Override
    protected void initializeGraphicalViewer() {
        EditPartViewer viewer = this.getGraphicalViewer();
        assert (this.projectModel != null);
        viewer.setContents(this.projectModel);
        viewer.addDropTargetListener(this.createTransferDropTargetListener());
        String titleTab = "";
        if (PersisterConnectionInfo.getPersisterConnectionInfo().getPersisterType().equalsIgnoreCase("LOCAL")) {
            titleTab = PersisterConnectionInfo.getPersisterConnectionInfo().getProjectName() + ":" + PersisterConnectionInfo.getPersisterConnectionInfo().getInitialLocal();
        } else {
            titleTab = PersisterConnectionInfo.getPersisterConnectionInfo().getProjectName() + ":" + PersisterConnectionInfo.getPersisterConnectionInfo().getInitialDis();
        }
        this.updateTitleTab(titleTab);
    }
