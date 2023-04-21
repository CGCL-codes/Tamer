    private IFile generateStyle(final Feature geoDataSet, final String rasterFile) {
        try {
            final String[] parts = rasterFile.split("\\.");
            String geoDataSetName = "";
            for (int i = 0; i <= parts.length - 2; i++) geoDataSetName += parts[i] + ".";
            geoDataSetName += "sld";
            final IFile iRaster = getRasterDataFolder().getFile(rasterFile);
            if (!iRaster.exists()) throw new IllegalStateException(Messages.RasterPerformFinishWorker_5 + iRaster.getLocation().toOSString());
            final IProject global = NofdpCorePlugin.getProjectManager().getBaseProject();
            final IFolder templateFolder = global.getFolder(NofdpIDSSConstants.NOFDP_PROJECT_GLOBAL_STYLES_FOLDER);
            final IFile iTemplate = templateFolder.getFile("templateRaster.sld");
            final File fTemplateSld = iTemplate.getLocation().toFile();
            final IFolder dataFolder = getRasterDataFolder();
            final IFolder styleFolder = dataFolder.getFolder(NofdpIDSSConstants.NOFDP_PROJECT_GEODATA_STYLES_FOLDER);
            if (!styleFolder.exists()) styleFolder.create(true, true, new NullProgressMonitor());
            final IFile iWorkingSld = styleFolder.getFile(geoDataSetName);
            final File fWorkingSld = iWorkingSld.getLocation().toFile();
            FileUtils.copyFile(fTemplateSld, fWorkingSld);
            WorkspaceSync.sync(getProject(), IResource.DEPTH_INFINITE);
            final IGeodataPageProvider pages = getPages();
            final PageSelectRasterSettings rasterSettings = (PageSelectRasterSettings) pages.getPage(RasterPages.COMMON_RASTER_SETTING_PAGE);
            final StyleReplacerRaster replacerRaster = new StyleReplacerRaster(rasterSettings, geoDataSet, iRaster, fWorkingSld);
            replacerRaster.replace();
            if (!iWorkingSld.exists()) throw new IllegalStateException();
            return iWorkingSld;
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }
