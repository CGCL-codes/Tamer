    private String copyFiles(final File geoDataFile) throws IOException, CoreException, GeoGridException {
        final String fileName = geoDataFile.getName();
        if (!fileName.contains(".")) throw new IllegalStateException(Messages.AbstractRasterData_1);
        final String[] parts = fileName.split("\\.");
        final File dirDest = m_rasterDataFolder.getLocation().toFile();
        final String geoDataSetName = BaseGeoUtils.getFileName(dirDest, parts[0]);
        final File[] files = BaseGeoUtils.getFiles(geoDataFile.getParentFile(), parts[0]);
        for (final File srcFile : files) {
            final String srcParts[] = srcFile.getName().split("\\.");
            if (srcParts.length != 2) continue;
            if ("asc".equals(srcParts[1].toLowerCase()) || "dat".equals(srcParts[1].toLowerCase())) {
                final File destFile = new File(dirDest, geoDataSetName + ".asc.bin");
                final PageCommonGeoDataSettings pCommonDetails = (PageCommonGeoDataSettings) m_pages.getPage(ShapePages.COMMON_DETAILS);
                final String crs = pCommonDetails.getSelectedCRS();
                final ConvertAscii2Binary converter = new ConvertAscii2Binary(srcFile.toURL(), destFile, 2, crs);
                converter.doConvert(new NullProgressMonitor());
            }
            final File destFile = new File(dirDest, geoDataSetName + "." + srcParts[1]);
            FileUtils.copyFile(srcFile, destFile);
            WorkspaceSync.sync(m_project, IResource.DEPTH_INFINITE);
        }
        return geoDataSetName;
    }
