    public Command getPasteCommand(IDiagramModel targetDiagramModel, GraphicalViewer viewer, int xMousePos, int yMousePos) {
        if (targetDiagramModel == null || viewer == null) {
            return null;
        }
        fTargetArchimateModel = targetDiagramModel.getArchimateModel();
        fDoCreateArchimateElementCopies = needsCopiedArchimateElements(targetDiagramModel);
        fXOffSet += 20;
        fYOffSet += 20;
        Hashtable<IDiagramModelObject, IDiagramModelObject> tmpSnapshotToNewObjectMapping = new Hashtable<IDiagramModelObject, IDiagramModelObject>();
        CompoundCommand result = new PasteCompoundCommand(Messages.CopySnapshot_0, tmpSnapshotToNewObjectMapping, viewer);
        for (IDiagramModelObject object : fDiagramModelSnapshot.getChildren()) {
            if (isValidPasteObject(targetDiagramModel, object)) {
                createPasteObjectCommand(targetDiagramModel, object, result, tmpSnapshotToNewObjectMapping);
            }
        }
        for (IDiagramModelConnection connection : fSnapshotToOriginalConnectionsMapping.keySet()) {
            createPasteConnectionCommand(connection, result, tmpSnapshotToNewObjectMapping);
        }
        return result;
    }
