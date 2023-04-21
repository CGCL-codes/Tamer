    private static IStyleReplacements getStyleReplacer(final IProject project, final Feature fDataSet, final String workingSld, final String templateSld) throws CoreException, IOException {
        final IFile iTemplate = BaseGeoUtils.getStyleTemplateForCategory(templateSld);
        final File fTemplate = new File(iTemplate.getLocation().toOSString());
        final IFolder subDir = BaseGeoUtils.getSubDirLocation(project, fDataSet);
        final IFolder fStyle = subDir.getFolder(NofdpIDSSConstants.NOFDP_PROJECT_GEODATA_STYLES_FOLDER);
        if (!fStyle.exists()) fStyle.create(true, false, new NullProgressMonitor());
        final IFile iDest = fStyle.getFile(workingSld);
        final File fDest = new File(iDest.getLocation().toOSString());
        FileUtils.copyFile(fTemplate, fDest);
        return new StyleReplacerCommon(fDataSet, fDest);
    }
