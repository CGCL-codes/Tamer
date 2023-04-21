    public CreateAction(final GraphicalViewer[] viewers) {
        super(Messages.CreateAction_Create);
        this.viewers = viewers;
        setToolTipText(Messages.CreateAction_ToggleCreation);
        setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ELEMENT));
        setId(ID);
    }
