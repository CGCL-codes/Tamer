    protected void hookGraphicalViewer() {
        getSelectionSynchronizer().addViewer(getGraphicalViewer());
        getSite().setSelectionProvider(getGraphicalViewer());
    }
