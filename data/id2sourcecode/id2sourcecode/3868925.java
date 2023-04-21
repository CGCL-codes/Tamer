    public static void cloneMap(final IProject project, final MyBasePool pool, final Feature f) throws Exception {
        final Object objMap = f.getProperty(IMap.QN_FILE_NAME);
        if (!(objMap instanceof String)) throw new IllegalStateException();
        final String sMap = (String) objMap;
        final IFolder folder = project.getFolder(IMaps.FOLDER);
        final IFile iFile = folder.getFile(sMap);
        if (!iFile.exists()) throw new IllegalStateException();
        final String sDestName = BaseGeoUtils.getFileName(folder, sMap.split("\\.")[0]) + "." + sMap.split("\\.")[1];
        final File fSrc = iFile.getLocation().toFile();
        final File fDest = new File(folder.getLocation().toFile(), sDestName);
        FileUtils.copyFile(fSrc, fDest);
        FeatureUtils.updateProperty(pool.getWorkspace(), f, IMap.QN_FILE_NAME, sDestName);
        WorkspaceSync.sync(project, IResource.DEPTH_INFINITE);
    }
