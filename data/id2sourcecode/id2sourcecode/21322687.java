    protected boolean importShapeFileToProject() throws IOException, CoreException {
        final WizardPage pFile = m_pages.getPage(ShapePages.COMMON_FILE_SELECTION);
        if (pFile == null || !(pFile instanceof PageSelectGeodataFile)) return false;
        final PageSelectGeodataFile geoFilePage = (PageSelectGeodataFile) pFile;
        final File file = geoFilePage.getSelectedFile();
        if (file == null) throw new IllegalStateException(Messages.AbstractShapePerformFinishWorker_2);
        final IFolder fDest = getDestinationFolder();
        if (fDest == null) throw new IllegalStateException(Messages.AbstractShapePerformFinishWorker_3);
        final String fileName = file.getName();
        if (!fileName.contains(".")) return false;
        final String[] parts = fileName.split("\\.");
        final File dirDest = fDest.getLocation().toFile();
        m_geoDataSetName = BaseGeoUtils.getFileName(dirDest, parts[0]);
        final File[] files = BaseGeoUtils.getFiles(file.getParentFile(), parts[0]);
        for (final File srcFile : files) {
            final String srcParts[] = srcFile.getName().split("\\.");
            if (srcParts.length != 2) continue;
            final File destFile = new File(dirDest, m_geoDataSetName + "." + srcParts[1]);
            FileUtils.copyFile(srcFile, destFile);
            WorkspaceSync.sync(m_project, IResource.DEPTH_INFINITE);
        }
        return true;
    }
