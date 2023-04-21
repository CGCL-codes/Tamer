    public IStatus execute(final IProgressMonitor monitor) throws CoreException, InvocationTargetException, InterruptedException {
        final IFolder dirGoogle = m_tmpDir.getFolder(DIR_GOOGLE_EARTH);
        try {
            final Feature[] googles = m_data.getGoogleEarth();
            for (final Feature google : googles) {
                final IFile iGoogle = GEUtils.getKMZFile(m_project, google);
                final File fGoogle = iGoogle.getLocation().toFile();
                final File dest = new File(dirGoogle.getLocation().toFile(), fGoogle.getName());
                FileUtils.copyFile(fGoogle, dest);
            }
        } catch (final IOException e) {
            NofdpCorePlugin.getDefault().getLog().log(StatusUtilities.statusFromThrowable(e));
            throw ExceptionHelper.getCoreException(IStatus.ERROR, getClass(), e.getMessage());
        }
        WorkspaceSync.sync(dirGoogle, IResource.DEPTH_INFINITE);
        return Status.OK_STATUS;
    }
