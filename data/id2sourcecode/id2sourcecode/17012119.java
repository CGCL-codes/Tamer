    protected OverviewOutlinePage getOverviewOutlinePage() {
        if (null == overviewOutlinePage && null != getGraphicalViewer()) {
            RootEditPart rootEditPart = getGraphicalViewer().getRootEditPart();
            if (rootEditPart instanceof ScalableFreeformRootEditPart) {
                overviewOutlinePage = new OverviewOutlinePage((ScalableFreeformRootEditPart) rootEditPart);
            }
        }
        return overviewOutlinePage;
    }
