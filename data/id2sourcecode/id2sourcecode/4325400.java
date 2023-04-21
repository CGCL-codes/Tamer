    protected void currentPageChanged() {
        getDelegatingCommandStack().setCurrentCommandStack(getCurrentPage().getCommandStack());
        getDelegatingZoomManager().setCurrentZoomManager(getZoomManager(getCurrentPage().getGraphicalViewer()));
    }
