    @Override
    public void createPartControl(final Composite parent) {
        _viewer = createGraphicalViewer(parent);
        setPartName(((ScannedMap) getEditorInput().getAdapter(ScannedMap.class)).getName());
    }
