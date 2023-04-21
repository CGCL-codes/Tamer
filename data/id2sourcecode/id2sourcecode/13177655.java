    private void duplicateStyle(final Style style) throws IOException {
        final IProject project = getProject();
        final IFolder mapFolder = project.getFolder(IMaps.FOLDER);
        final String href = style.getHref();
        final IFile fStyle = mapFolder.getFile(href);
        if (!fStyle.exists()) throw new IllegalStateException(Messages.IsarAppMapExportConfig_0 + fStyle.getLocation().toOSString());
        final IFolder styleFolder = (IFolder) fStyle.getParent();
        String styleName = getImportedFeatureFileName() + "_" + fStyle.getName();
        styleName = BaseGeoUtils.getFileName(styleFolder, styleName);
        final File fSrc = fStyle.getLocation().toFile();
        final File fDest = styleFolder.getFile(styleName).getLocation().toFile();
        FileUtils.copyFile(fSrc, fDest);
        final String[] parts = href.split("/");
        parts[parts.length - 1] = styleName;
        String newRef = "";
        for (final String string : parts) newRef += string + "/";
        newRef = StringUtilities.chomp(newRef);
        style.setHref(newRef);
        WorkspaceSync.sync(styleFolder, IResource.DEPTH_INFINITE);
        WorkspaceSync.sync(mapFolder, IResource.DEPTH_INFINITE);
    }
