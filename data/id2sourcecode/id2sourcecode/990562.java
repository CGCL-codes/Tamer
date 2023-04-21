    public IFile[] getDocuments(final IFile iReportFile) throws Exception {
        final ReportFolders folder = new ReportFolders(NofdpCorePlugin.getProjectManager().getActiveProject());
        final IFolder iTempFolder = folder.getTempFolder(true);
        final IFile iTempReportFile = iTempFolder.getFile(iReportFile.getName());
        FileUtils.copyFile(iReportFile.getLocation().toFile(), iTempReportFile.getLocation().toFile());
        WorkspaceSync.sync(iTempFolder, IResource.DEPTH_INFINITE);
        if (!iTempReportFile.exists()) throw new IllegalStateException(Messages.OpenOfficeExporter_1);
        configureJob(iTempReportFile);
        final IFile jobFile = writeJobFile(iTempFolder);
        processJobFile(jobFile);
        WorkspaceSync.sync(iTempFolder, IResource.DEPTH_INFINITE);
        final IFile resultZip = iTempFolder.getFile(OpenOfficeExporter.RESULT_ZIP_NAME);
        if (!resultZip.exists()) throw new IllegalStateException(Messages.OpenOfficeExporter_2);
        final IFolder iDocumentResultFolder = iTempFolder.getFolder(Messages.OpenOfficeExporter_3);
        final ZipFile zf = new ZipFile(resultZip.getLocation().toFile());
        ZipUtils.unpack(zf, iDocumentResultFolder.getLocation().toFile());
        zf.close();
        WorkspaceSync.sync(iTempFolder, IResource.DEPTH_INFINITE);
        final CollectFilesVisitor visitor = new CollectFilesVisitor();
        iDocumentResultFolder.accept(visitor);
        return visitor.getFiles();
    }
