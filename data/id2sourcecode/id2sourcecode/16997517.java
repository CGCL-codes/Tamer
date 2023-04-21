    @Override
    protected void initializeGraphicalViewer() {
        EditPartViewer viewer = this.getGraphicalViewer();
        assert (this.projectModel != null);
        viewer.setContents(this.projectModel);
        viewer.addDropTargetListener(this.createTransferDropTargetListener());
        String titleTab = "";
        if (Settings.getPersisterType().equalsIgnoreCase("LOCAL")) {
            titleTab = this.projectModel.getProjectDataObject().getName() + ":" + Settings.getInitialLocal();
        } else {
            titleTab = this.projectModel.getProjectDataObject().getName() + ":" + Settings.getInitialDis();
        }
        this.updateTitleTab(titleTab);
    }
