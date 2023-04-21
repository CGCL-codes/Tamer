    public IStatus execute(final IProgressMonitor monitor) {
        final File src = (File) m_model.getValue(TSM_KEY.eZmlFile);
        final File dirDest = (File) m_model.getValue(TSM_KEY.eDestinationDir);
        if (!src.exists()) throw new IllegalStateException(Messages.TsmZmlImportWorker_0 + src.getAbsoluteFile());
        if (!dirDest.exists()) throw new IllegalStateException(Messages.TsmZmlImportWorker_1 + dirDest.getAbsoluteFile());
        final File dest = new File(dirDest, src.getName());
        if (dest.exists()) throw new IllegalStateException(Messages.TsmZmlImportWorker_2 + dest.getAbsolutePath());
        try {
            FileUtils.copyFile(src, dest);
        } catch (final IOException e) {
            NofdpCorePlugin.getDefault().getLog().log(StatusUtilities.statusFromThrowable(e));
            return Status.CANCEL_STATUS;
        }
        final IProject project = NofdpCorePlugin.getProjectManager().getActiveProject();
        WorkspaceSync.sync(project, IResource.DEPTH_INFINITE);
        return Status.OK_STATUS;
    }
