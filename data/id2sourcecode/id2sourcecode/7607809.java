    private void generateInundationAreaStyle(final IFolder importDataFolder) throws IOException, CoreException {
        final IProject base = NofdpCorePlugin.getProjectManager().getBaseProject();
        final IFile baseFile = base.getFile(".styles/template_inundationarea.sld");
        final IFile iSld = importDataFolder.getFile("inundationArea.sld");
        FileUtils.copyFile(baseFile.getLocation().toFile(), iSld.getLocation().toFile());
        WorkspaceSync.sync(iSld, IResource.DEPTH_ONE);
        final IGeodataModel model = m_provider.getGeodataModel();
        final IGeodataCategory[] categories = model.getCategories().getCategories(new QName[] { IGeodataCategories.QN_SUBCATEGORY_INUNDATION_AREA });
        if (categories.length != 1) throw ExceptionHelper.getCoreException(IStatus.ERROR, this.getClass(), Messages.FloodZoneWorker_2);
        final IStyleReplacements replacer = new StyleReplacerInundationArea(categories[0], iSld.getLocation().toFile());
        replacer.replace();
        WorkspaceSync.sync(iSld, IResource.DEPTH_ONE);
    }
