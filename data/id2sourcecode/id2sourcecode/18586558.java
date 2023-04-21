    @Override
    public void createPartControl(Composite parent) {
        loadResources();
        container = new Composite(parent, SWT.NONE);
        container.setLayout(new FillLayout());
        final SashForm sash = new SashForm(container, SWT.HORIZONTAL);
        createGridTreeViewer(sash);
        createGraphicalViewer(sash);
        getSite().setSelectionProvider(this);
    }
