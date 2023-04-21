    @Override
    public void run() {
        fGraphicalViewer = (GraphicalViewer) getWorkbenchPart().getAdapter(GraphicalViewer.class);
        fOldParent = fGraphicalViewer.getControl().getParent();
        fOldPaletteViewer = fGraphicalViewer.getEditDomain().getPaletteViewer();
        fGraphicalViewer.setProperty("full_screen", true);
        addKeyBindings();
        fGraphicalViewer.getContextMenu().addMenuListener(contextMenuListener);
        fGraphicalViewer.getControl().addKeyListener(keyListener);
        int style = SWT.APPLICATION_MODAL | SWT.SHELL_TRIM;
        if (PlatformUtils.isMac()) {
            style |= SWT.ON_TOP;
        }
        fNewShell = new Shell(Display.getCurrent(), style);
        fNewShell.setFullScreen(true);
        fNewShell.setMaximized(true);
        fNewShell.setLayout(new FillLayout());
        fNewShell.setImage(IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_APP_128));
        fGraphicalViewer.getControl().setParent(fNewShell);
        fNewShell.layout();
        fNewShell.open();
        fFloatingPalette = new FloatingPalette((IDiagramModelEditor) ((DefaultEditDomain) fGraphicalViewer.getEditDomain()).getEditorPart(), fNewShell);
        if (fFloatingPalette.getPaletteState().isOpen) {
            fFloatingPalette.open();
        }
        fOldParent.getShell().setVisible(false);
        getWorkbenchPart().getSite().getWorkbenchWindow().getPartService().addPartListener(partListener);
        fNewShell.setFocus();
    }
