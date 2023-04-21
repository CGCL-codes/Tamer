    public Visualizer(Composite parent, ModelProvider modelProvider, MenuManager contextMenu, IWorkbenchPage workbenchPage, PaletteRoot palette, ProcessDesignerInplaceManager inplaceManager) {
        myModelProvider = modelProvider;
        myModelProvider.addRootChangeListener(this);
        parent.setLayout(new GridLayout(1, false));
        myViewer = new ScrollingGraphicalViewer() {

            @Override
            public Handle findHandleAt(Point p) {
                Handle handle = super.findHandleAt(p);
                if (handle instanceof ConnectionHandle) {
                    return null;
                } else {
                    return handle;
                }
            }
        };
        myViewer.setEditPartFactory(new EditPartFactoryImpl(inplaceManager));
        ScalableFreeformRootEditPart rootEditPart = new ScalableFreeformRootEditPart();
        ((ConnectionLayer) rootEditPart.getLayer(LayerConstants.CONNECTION_LAYER)).setConnectionRouter(new BendpointConnectionRouter());
        myViewer.setRootEditPart(rootEditPart);
        myViewer.setEditDomain(new EditDomain());
        PaletteViewerProvider paletteViewerProvider = new PaletteViewerProvider(myViewer.getEditDomain()) {

            @Override
            protected void configurePaletteViewer(PaletteViewer viewer) {
                super.configurePaletteViewer(viewer);
            }
        };
        PreferenceStoreBasedFlyoutPreferences palettePrefs = new PreferenceStoreBasedFlyoutPreferences(UIPlugin.getDefault().getPreferenceStore());
        FlyoutPaletteComposite paletteComposite = new FlyoutPaletteComposite(parent, SWT.BORDER, workbenchPage, paletteViewerProvider, palettePrefs);
        paletteComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
        Composite composite = new Composite(paletteComposite, SWT.NONE);
        GridLayout gridLayout = new GridLayout();
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        gridLayout.verticalSpacing = 0;
        composite.setLayout(gridLayout);
        CoolBar coolbar = new CoolBar(composite, SWT.FLAT);
        coolbar.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        myCoolBarManager = new CoolBarManager(coolbar);
        Label separator = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
        separator.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        myViewer.createControl(composite);
        paletteComposite.setGraphicalControl(composite);
        myViewer.getEditDomain().setPaletteRoot(palette);
        setFilterListeners(palette);
        myViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
        myViewer.getControl().setBackground(ColorConstants.white);
        myViewer.setContextMenu(contextMenu);
        mySelectionProvider = new StructuredSelectionConverter(myViewer, new ObjectConverterImpl()) {

            @Override
            protected void setConvertedSelection(ISelection selection, boolean forceListen) {
                super.setConvertedSelection(selection, forceListen);
                if (!selection.isEmpty()) {
                    EditPart editPart = (EditPart) ((IStructuredSelection) selection).getFirstElement();
                    myViewer.reveal(editPart);
                }
            }

            @Override
            protected IStructuredSelection convertFromOriginal(IStructuredSelection originalSelection) {
                IStructuredSelection selection = super.convertFromOriginal(originalSelection);
                if (selection.isEmpty()) {
                    if (myModelProvider.getRootElementUuid() != null) {
                        IElementWrapper elementWrapper = DataCacheManager.getWrapperByUuid(myModelProvider.getRootElementUuid());
                        if ((elementWrapper != null) && !elementWrapper.isGhost()) {
                            return new StructuredSelection(elementWrapper);
                        }
                    }
                    return StructuredSelection.EMPTY;
                } else if ((selection.size() == 1) && FORBID_SELECTION.equals(selection.getFirstElement())) {
                    return StructuredSelection.EMPTY;
                } else {
                    return selection;
                }
            }
        };
        myViewer.addDropTargetListener(new XElementTransferDropTargetListener(myViewer));
        refresh(true);
    }
