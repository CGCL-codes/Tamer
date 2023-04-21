    public PasteAction(IWorkbenchPart part, GraphicalViewer viewer) {
        super(part);
        fGraphicalViewer = viewer;
        setText(Messages.PasteAction_0);
        setId(ActionFactory.PASTE.getId());
        ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
        setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
        setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE_DISABLED));
        setEnabled(false);
        addMouseListener();
    }
