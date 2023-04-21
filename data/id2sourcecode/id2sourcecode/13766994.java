    private OverviewOutlinePage getOverviewOutlinePage() {
        if ((this.overviewOutlinePage == null) && (getGraphicalViewer() != null)) {
            RootEditPart root = getGraphicalViewer().getRootEditPart();
            if (root instanceof ScalableFreeformRootEditPart) {
                this.overviewOutlinePage = new OverviewOutlinePage((ScalableFreeformRootEditPart) root);
            }
        }
        return this.overviewOutlinePage;
    }
