    @Override
    public void initializeGraphicalViewer() {
        GraphicalViewer viewer = getGraphicalViewer();
        PObjectReference reference = ((ChainEditorInput) this.getEditorInput()).getInput();
        String attachment = "";
        try {
            if (reference instanceof PSxSObjectReference) {
                chain = ((PSxSObjectReference) reference).resolve(chainRepository, false, false, false);
                if (chain != null && reference.attachmentExists(chainRepository, chainAttachmentID)) {
                    attachment = ((PSxSObjectReference) reference).getAttachment(chainRepository, chainAttachmentID, false);
                }
            } else {
                chain = reference.resolve((IRepository<Chain>) chainRepository, false, false);
                if (chain != null && reference.attachmentExists(chainRepository, chainAttachmentID)) {
                    attachment = reference.getAttachment(chainRepository, chainAttachmentID);
                }
            }
        } catch (PObjectNotFoundException e) {
        }
        chainModel = generateModelDependencies(attachment, false);
        chainModel.resetDirty();
        viewer.addDropTargetListener(new ChainTransferDropTargetListener(viewer));
        viewer.setContents(chainModel);
        setPartName(chainModel.getContent().getLabel() + " " + chainModel.getContent().getVersion());
    }
