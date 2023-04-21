    public static void cloneDocument(final MyBasePool pool, final Feature fDocument) throws Exception {
        if (fDocument == null) return;
        final Object objFileName = fDocument.getProperty(IDocumentMember.QN_FILE_NAME);
        if (!(objFileName instanceof String) || ((String) objFileName).equals("")) return;
        String sFileName = (String) objFileName;
        final IProject project = NofdpCorePlugin.getProjectManager().getProjectToEdit();
        WorkspaceSync.sync(project, IResource.DEPTH_INFINITE);
        final IFolder documentFolder = project.getFolder(IProjectInfoModel.DOCUMENT_FOLDER);
        final File srcFile = new File(documentFolder.getFile(sFileName).getLocation().toOSString());
        final Feature fParent = fDocument.getParent();
        final IRelationType relation = (IRelationType) fParent.getFeatureType().getProperty(IProjectInfoModel.QN_DOCUMENTS);
        final Feature fClone = FeatureHelper.cloneFeature(fParent, relation, fDocument);
        final String newName = CloneRenaming.getNewName(sFileName);
        FeatureUtils.updateProperty(pool.getWorkspace(), fClone, IDocumentMember.QN_NAME, newName);
        sFileName = "Copy of " + sFileName;
        File destFile = new File(documentFolder.getFile(sFileName).getLocation().toOSString());
        if (destFile.exists()) {
            int count = 1;
            while (destFile.exists()) {
                destFile = new File(documentFolder.getFile(count + " - " + sFileName).getLocation().toOSString());
                count++;
            }
            FeatureUtils.updateProperty(pool.getWorkspace(), fClone, IDocumentMember.QN_FILE_NAME, --count + " - " + sFileName);
        } else FeatureUtils.updateProperty(pool.getWorkspace(), fClone, IDocumentMember.QN_FILE_NAME, sFileName);
        FileUtils.copyFile(srcFile, destFile);
        WorkspaceSync.sync(project, IResource.DEPTH_INFINITE);
    }
