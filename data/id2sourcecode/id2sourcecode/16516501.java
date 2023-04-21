    public IStatus execute(final IProgressMonitor monitor) throws CoreException, InvocationTargetException, InterruptedException {
        IFolder dirReport;
        try {
            dirReport = m_tmpDir.getFolder(DIR_REPORT);
            final REPORT_FORMAT[] formats = m_data.getReportFormats();
            final OpenOfficeExporter exporter = new OpenOfficeExporter(m_project, formats);
            final Feature[] reports = m_data.getReports();
            for (final Feature report : reports) {
                final IFile iReportFile = ReportingUtils.getReportFile(m_project, report);
                final IFile[] files = exporter.getDocuments(iReportFile);
                for (final IFile file : files) {
                    final File iFile = file.getLocation().toFile();
                    final File dest = new File(dirReport.getLocation().toFile(), iFile.getName());
                    FileUtils.copyFile(iFile, dest);
                }
            }
        } catch (final Exception e) {
            NofdpCorePlugin.getDefault().getLog().log(StatusUtilities.statusFromThrowable(e));
            throw ExceptionHelper.getCoreException(IStatus.ERROR, getClass(), e.getMessage());
        }
        WorkspaceSync.sync(dirReport, IResource.DEPTH_INFINITE);
        return Status.OK_STATUS;
    }
