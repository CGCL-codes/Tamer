    public IStatus execute(final IProgressMonitor monitor) throws CoreException, InvocationTargetException, InterruptedException {
        final IFolder dirGeoData = m_tmpDir.getFolder(DIR_GEODATA);
        final IGeodataSet[] geoDataSets = m_data.getGeoData();
        for (final IGeodataSet gds : geoDataSets) {
            try {
                final DATA_TYPE type = gds.getType();
                final IFile iGds = gds.getIFile();
                final IFolder iParent = (IFolder) iGds.getParent();
                final File parent = iParent.getLocation().toFile();
                final String name = iGds.getName();
                final int lastIndexOf = name.lastIndexOf(".");
                final String fileName = name.substring(0, lastIndexOf + 1);
                final File[] files = BaseGeoUtils.getFiles(parent, fileName);
                final IFolder iDestFolder = createFolderStructure(dirGeoData, iParent);
                final File destFolder = iDestFolder.getLocation().toFile();
                for (File file : files) {
                    try {
                        if (DATA_TYPE.eRaster.equals(type)) {
                            final String fn = file.getName().toLowerCase();
                            if (fn.endsWith(".bin")) {
                                file = convert2Asc(file, monitor);
                            } else if (fn.endsWith(".gml")) continue;
                        }
                        final File dest = new File(destFolder, file.getName());
                        FileUtils.copyFile(file, dest);
                    } catch (final Exception e) {
                        NofdpCorePlugin.getDefault().getLog().log(StatusUtilities.statusFromThrowable(e));
                    }
                }
            } catch (final IOException e) {
                NofdpCorePlugin.getDefault().getLog().log(StatusUtilities.statusFromThrowable(e));
                throw ExceptionHelper.getCoreException(IStatus.ERROR, getClass(), e.getMessage());
            }
            WorkspaceSync.sync(m_tmpDir, IResource.DEPTH_INFINITE);
        }
        return Status.OK_STATUS;
    }
