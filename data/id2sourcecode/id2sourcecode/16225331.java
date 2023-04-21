    private void generateStyle() throws IOException, CoreException {
        final IProject base = NofdpCorePlugin.getProjectManager().getBaseProject();
        final IFile baseFile = base.getFile(".styles/template_inundationduration.sld");
        final IFile iSld = m_workingFolder.getFile("eventBasedDuration.sld");
        FileUtils.copyFile(baseFile.getLocation().toFile(), iSld.getLocation().toFile());
        WorkspaceSync.sync(iSld, IResource.DEPTH_ONE);
        final PoolGeoData pool = (PoolGeoData) NofdpCorePlugin.getProjectManager().getPool(POOL_TYPE.eGeodata);
        final IGeodataModel model = pool.getModel();
        final IGeodataCategory[] categories = model.getCategories().getCategories(new QName[] { IGeodataCategories.QN_SUBCATEGORY_INUNDATION_DURATION });
        if (categories.length != 1) throw ExceptionHelper.getCoreException(IStatus.ERROR, this.getClass(), Messages.IDFEventWorker_31);
        final IStyleReplacements replacer = new StyleReplacerEventBasedInundationDuration(m_wrappers, categories[0], iSld.getLocation().toFile());
        replacer.replace();
        WorkspaceSync.sync(iSld, IResource.DEPTH_ONE);
    }
