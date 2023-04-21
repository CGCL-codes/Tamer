    protected DelegatingZoomManager getDelegatingZoomManager() {
        if (null == delegatingZoomManager) {
            delegatingZoomManager = new DelegatingZoomManager();
            if (null != getCurrentPage() && null != getCurrentPage().getGraphicalViewer()) delegatingZoomManager.setCurrentZoomManager(getZoomManager(getCurrentPage().getGraphicalViewer()));
        }
        return delegatingZoomManager;
    }
