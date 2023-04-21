    private IFile generateMap(final IFile shapeFile, final String viewName) throws IOException, JAXBException, CoreException, SAXException, ParserConfigurationException {
        final IFolder mapFolder = m_project.getFolder(IMaps.FOLDER);
        final IFile mapTemplate = mapFolder.getFile(IMaps.ID_MAP_WATER_STORAGE_TEMPLATE);
        final File fTemplate = mapTemplate.getLocation().toFile();
        final IFolder parent = (IFolder) shapeFile.getParent();
        WorkspaceSync.sync(parent, IResource.DEPTH_INFINITE);
        final IFile map = parent.getFile(WSGenerator.WATER_STORAGE_FILE_NAME + ".gmt");
        final File fMap = map.getLocation().toFile();
        FileUtils.copyFile(fTemplate, fMap);
        WorkspaceSync.sync(parent, IResource.DEPTH_INFINITE);
        final StyledLayerType layer = getStyleLayer(viewName);
        MapTool.addMapLayer(map, layer, ADD_MAP_LAYER_AT.eFront);
        WorkspaceSync.sync(m_project, IResource.DEPTH_INFINITE);
        return map;
    }
