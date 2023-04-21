    public static AsteriskDiagramEditor openDebugEditor(String diagramName, String fileExt, String currentPath, boolean activate) throws IOException {
        String suffix = "_debug." + fileExt;
        AsteriskDiagramEditorPlugin.getDefault().logInfo("Trying to create temp file " + (diagramName + fileExt));
        File tempFile = null;
        try {
            tempFile = File.createTempFile(diagramName, suffix);
        } catch (Exception e) {
            throw new IOException("Couldn't create temp file " + (diagramName + fileExt));
        }
        AsteriskDiagramEditorPlugin.getDefault().logInfo("Created temp file " + tempFile.getAbsolutePath());
        tempFile.deleteOnExit();
        FileUtils.copyFile(currentPath, tempFile.getAbsolutePath());
        SafiWorkshopEditorUtil.openDiagram(URI.createFileURI(tempFile.getAbsolutePath()), true, activate);
        IEditorPart activeEditor = AsteriskDiagramEditorPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        if (!(activeEditor instanceof AsteriskDiagramEditor)) {
            return null;
        }
        AsteriskDiagramEditor asteriskDiagramEditor = ((AsteriskDiagramEditor) activeEditor);
        HandlerEditPart handlerEditPart = (HandlerEditPart) asteriskDiagramEditor.getDiagramEditPart();
        handlerEditPart.setDebug(true);
        for (Iterator iter = handlerEditPart.getChildren().iterator(); iter.hasNext(); ) {
            Object obj = iter.next();
            if (obj instanceof IGraphicalEditPart) {
                ((IGraphicalEditPart) obj).disableEditMode();
            }
        }
        return asteriskDiagramEditor;
    }
