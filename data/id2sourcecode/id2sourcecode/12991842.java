    private void doFooStuff(final Style style) throws IOException {
        style.setStyle("conflict.shp");
        final IProject project = getProject();
        final IFolder mapFolder = project.getFolder(IMaps.FOLDER);
        final String href = style.getHref();
        final IFile iDest = mapFolder.getFile(href);
        final IFolder geoDataFolder = (IFolder) iDest.getParent().getParent();
        final String nameStyle = iDest.getName();
        final IFile iSrc = geoDataFolder.getFile(nameStyle);
        if (!iSrc.exists()) throw new IllegalStateException(Messages.CDDelegateMapExportConfig_1 + iSrc.getLocation().toOSString());
        final File fSrc = iSrc.getLocation().toFile();
        final File fDest = iDest.getLocation().toFile();
        FileUtils.copyFile(fSrc, fDest);
        WorkspaceSync.sync(geoDataFolder, IResource.DEPTH_INFINITE);
        WorkspaceSync.sync(mapFolder, IResource.DEPTH_INFINITE);
    }
