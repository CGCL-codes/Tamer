    public CreateDerivedRelationAction(IWorkbenchPart part) {
        super(part);
        setText(TEXT);
        setId(ID);
        setSelectionProvider((ISelectionProvider) part.getAdapter(GraphicalViewer.class));
        setImageDescriptor(IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_DERIVED_16));
    }
