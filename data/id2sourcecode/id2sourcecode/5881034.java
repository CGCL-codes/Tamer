    private void generateStyle(final IFolder importDataFolder) throws IOException, CoreException {
        final IProject base = NofdpCorePlugin.getProjectManager().getBaseProject();
        final IFile baseFile = base.getFile(".styles/template_inundationfrequency.sld");
        final IFile iSld = importDataFolder.getFile("frequencies.sld");
        FileUtils.copyFile(baseFile.getLocation().toFile(), iSld.getLocation().toFile());
        WorkspaceSync.sync(iSld, IResource.DEPTH_ONE);
        final PoolGeoData pool = (PoolGeoData) NofdpCorePlugin.getProjectManager().getPool(POOL_TYPE.eGeodata);
        final IGeodataModel model = pool.getModel();
        final IGeodataCategory[] categories = model.getCategories().getCategories(new QName[] { IGeodataCategories.QN_SUBCATEGORY_INUNDATION_FREQ });
        if (categories.length != 1) throw ExceptionHelper.getCoreException(IStatus.ERROR, this.getClass(), Messages.IDFWorker_29);
        final IStyleReplacements replacer = new StyleReplacerInundationFrequency(categories[0], iSld.getLocation().toFile(), m_wrappers.values().toArray(new MyGridCategoryWrapper[] {}));
        replacer.replace();
        WorkspaceSync.sync(iSld, IResource.DEPTH_ONE);
    }
