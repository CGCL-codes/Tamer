    public boolean reopenDraft(Document newDraft) {
        document = newDraft;
        PaletteBuilder.populatePalette(document.getTemplate(), getPaletteRoot());
        EditPartViewer viewer = getGraphicalViewer();
        viewer.setContents(getDiagram());
        viewer.addDropTargetListener(createTransferDropTargetListener(getGraphicalViewer()));
        return true;
    }
