    private Section createReactumSection(final IManagedForm managedForm) {
        final Section section = createSection(managedForm);
        final ToolBar tbar = new ToolBar(section, SWT.FLAT | SWT.HORIZONTAL);
        ToolItem titem = new ToolItem(tbar, SWT.NULL);
        titem.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_CUT));
        titem = new ToolItem(tbar, SWT.PUSH);
        titem.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_COPY));
        titem = new ToolItem(tbar, SWT.SEPARATOR);
        titem = new ToolItem(tbar, SWT.PUSH);
        titem.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_DELETE));
        section.setTextClient(tbar);
        final Composite client = (Composite) section.getClient();
        client.setLayout(new FillLayout());
        section.setText("Reactum");
        reactumGraph = createGraphicalViewer(client);
        return section;
    }
