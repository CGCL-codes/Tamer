    @Override
    public void createPartControl(final Composite parent) {
        final Composite split = new Composite(parent, SWT.NONE);
        final GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.numColumns = 2;
        split.setLayout(layout);
        _viewer = createGraphicalViewer(split);
        _viewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
        final NamedUuidEntity data = (NamedUuidEntity) getEditorInput().getAdapter(NamedUuidEntity.class);
        final Profile profile = data instanceof ScannedMap ? EditPlugin.getBL().getProfile((ScannedMap) data) : EditPlugin.getBL().getDefaultProfile();
        _palette = new MapPalette(getEditDomain(), profile);
        final PaletteViewer paletteViewer = _palette.createPaletteViewer(split);
        final GridData gd = new GridData(GridData.FILL_VERTICAL);
        gd.widthHint = 175;
        paletteViewer.getControl().setLayoutData(gd);
        setPartName(data.getName());
    }
