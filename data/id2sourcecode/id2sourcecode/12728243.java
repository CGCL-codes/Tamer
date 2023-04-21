    protected TestOverviewOutlinePage getTestOverviewOutlinePage() {
        if (null == testOverviewOutlinePage && null != getGraphicalViewer()) {
            RootEditPart rootEditPart = getGraphicalViewer().getRootEditPart();
            if (rootEditPart instanceof ScalableFreeformRootEditPart) {
                testOverviewOutlinePage = new TestOverviewOutlinePage((ScalableFreeformRootEditPart) rootEditPart);
            }
        }
        return testOverviewOutlinePage;
    }
