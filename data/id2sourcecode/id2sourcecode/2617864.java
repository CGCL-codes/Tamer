    private void close() {
        fFloatingPalette.close();
        fGraphicalViewer.getContextMenu().removeMenuListener(contextMenuListener);
        fGraphicalViewer.getControl().removeKeyListener(keyListener);
        getWorkbenchPart().getSite().getWorkbenchWindow().getPartService().removePartListener(partListener);
        fGraphicalViewer.getEditDomain().setPaletteViewer(fOldPaletteViewer);
        fGraphicalViewer.getControl().setParent(fOldParent);
        fOldParent.layout();
        fNewShell.dispose();
        fGraphicalViewer.setProperty("full_screen", null);
        fOldParent.getShell().setVisible(true);
        getWorkbenchPart().getSite().getWorkbenchWindow().getShell().setFocus();
    }
