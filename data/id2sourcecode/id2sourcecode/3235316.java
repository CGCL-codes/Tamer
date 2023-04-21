            @Override
            public IStatus runInUIThread(IProgressMonitor monitor) {
                try {
                    monitor.beginTask(Messages.ReportBuilder_0, 10);
                    final ReportFolders folders = new ReportFolders(m_wizard.getActiveProject());
                    final MyReportJob myReportJob = new MyReportJob(folders);
                    final DocumentTreeCategory[] categories = m_parts.getSelectedCategories();
                    final DocumentTreeCategory[] maps = m_maps.getSelectedMaps();
                    final List<DocumentTreeCategory> merge = new ArrayList<DocumentTreeCategory>();
                    for (final DocumentTreeCategory dtc : categories) merge.add(dtc);
                    for (final DocumentTreeCategory dtc : maps) merge.add(dtc);
                    final ModuleReportBuilder[] builder = ModuleReportBuilder.getModuleReportBuilders(m_wizard, m_maps, merge.toArray(new DocumentTreeCategory[] {}), m_parts.getSelectedVariants(), folders);
                    for (final ModuleReportBuilder b : builder) {
                        final DocumentType[] types = b.getDocumentTypes();
                        myReportJob.addDocumentTemplates(types);
                    }
                    final IFile iJobFile = myReportJob.writeJobFile();
                    processJobFile(iJobFile);
                    final IFolder iTmpFolder = (IFolder) iJobFile.getParent();
                    WorkspaceSync.sync(iTmpFolder, IResource.DEPTH_INFINITE);
                    final IFile iResultZip = iTmpFolder.getFile(MyReportJob.RESULT_ZIP_NAME);
                    if (!iResultZip.exists()) throw new IllegalStateException(Messages.ReportBuilder_1 + iResultZip.getLocation().toOSString());
                    ZipUtilities.unzip(iResultZip.getLocation().toFile(), iTmpFolder.getLocation().toFile());
                    WorkspaceSync.sync(iTmpFolder, IResource.DEPTH_INFINITE);
                    final IFile iTempResultDocument = iTmpFolder.getFile(MyReportJob.RESULT_DOCUMENT_ODT);
                    if (!iTempResultDocument.exists()) throw new IllegalStateException(Messages.ReportBuilder_2 + iTempResultDocument.getLocation().toOSString());
                    final IFolder iFolderDocuments = m_wizard.getActiveProject().getFolder(NofdpIDSSConstants.NOFDP_PROJECT_REPORTING_DOCUMENTS_FOLDER_PATH);
                    WorkspaceSync.sync(iFolderDocuments, IResource.DEPTH_INFINITE);
                    final String fileName = BaseGeoUtils.getFileName(iFolderDocuments, m_parts.getReportName()) + "." + iTempResultDocument.getFileExtension();
                    final IFile iResultDocument = iFolderDocuments.getFile(fileName);
                    FileUtils.copyFile(iTempResultDocument.getLocation().toFile(), iResultDocument.getLocation().toFile());
                    WorkspaceSync.sync(iFolderDocuments, IResource.DEPTH_INFINITE);
                    final PoolReporting pool = (PoolReporting) NofdpCorePlugin.getProjectManager().getPool(POOL_TYPE.eReporting);
                    ReportingGmlTools.createResultDocumentFeature(pool, m_parts.getReportName(), m_parts.getReportDescription(), iResultDocument.getName());
                } catch (final Exception e) {
                    NofdpCorePlugin.getDefault().getLog().log(StatusUtilities.statusFromThrowable(e));
                    return new Status(IStatus.ERROR, Messages.ReportBuilder_4, e.getMessage());
                }
                return Status.OK_STATUS;
            }
