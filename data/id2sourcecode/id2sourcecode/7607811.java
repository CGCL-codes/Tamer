    private void generateInundationDepthStyle(final IFolder importDataFolder) throws IOException, CoreException {
        final IProject base = NofdpCorePlugin.getProjectManager().getBaseProject();
        final IFile baseFile = base.getFile(".styles/template_inundationdepth.sld");
        final IFile iSld = importDataFolder.getFile("inundationDepth.sld");
        FileUtils.copyFile(baseFile.getLocation().toFile(), iSld.getLocation().toFile());
        WorkspaceSync.sync(iSld, IResource.DEPTH_ONE);
        final IGeodataModel model = m_provider.getGeodataModel();
        final IGeodataCategory[] categories = model.getCategories().getCategories(new QName[] { IGeodataCategories.QN_SUBCATEGORY_INUNDATION_DEPTH });
        if (categories.length != 1) throw ExceptionHelper.getCoreException(IStatus.ERROR, this.getClass(), Messages.FloodZoneWorker_2);
        final IStyleReplacements replacer = new StyleReplacerInundationDepth(categories[0], iSld.getLocation().toFile());
        replacer.replace();
        WorkspaceSync.sync(iSld, IResource.DEPTH_ONE);
    }
