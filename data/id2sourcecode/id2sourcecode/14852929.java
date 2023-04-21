    public static IFile updateIsarMap(final IProject project, final Feature fGeodataSet) throws JAXBException, IOException, CoreException, SAXException, ParserConfigurationException {
        final IFolder mapFolder = project.getFolder(IMaps.FOLDER);
        final IFile mapTemplate = mapFolder.getFile(IsarUtils.ISAR_APP_MAP_FILE);
        final File fTemplate = mapTemplate.getLocation().toFile();
        final IFolder tmpFolder = project.getFolder(NofdpIDSSConstants.NOFDP_TMP_FOLDER);
        if (!tmpFolder.exists()) tmpFolder.create(true, true, new NullProgressMonitor());
        WorkspaceSync.sync(tmpFolder, IResource.DEPTH_INFINITE);
        final IFile map = tmpFolder.getFile(IsarUtils.ISAR_APP_MAP_FILE);
        final File fMap = map.getLocation().toFile();
        FileUtils.copyFile(fTemplate, fMap);
        WorkspaceSync.sync(tmpFolder, IResource.DEPTH_INFINITE);
        final List<File> slds = new LinkedList<File>();
        final File riverSld = IsarUtils.getPhysicalRiverQualityRiverStyle(project, fGeodataSet);
        slds.add(riverSld);
        WorkspaceSync.sync(project, IResource.DEPTH_INFINITE);
        for (int i = 5; i > 0; i--) {
            final IStyleReplacements srm = IsarUtils.getStyleReplacer(project, fGeodataSet, IsarUtils.getStyleMeaName(i), IsarUtils.getTemplateStyleMeaName(i));
            if (!srm.replace()) throw new IllegalStateException(Messages.IsarUtils_48);
            slds.add(srm.getSld());
        }
        final IStyleReplacements srd = IsarUtils.getStyleReplacer(project, fGeodataSet, IsarUtils.NOFDP_DEF_STYLE_NAME, IsarUtils.NOFDP_DEF_TEMPLATE_NAME);
        if (!srd.replace()) throw new IllegalStateException(Messages.IsarUtils_47);
        slds.add(srd.getSld());
        WorkspaceSync.sync(project, IResource.DEPTH_INFINITE);
        for (final File sld : slds) {
            final StyledLayerType layer = IsarUtils.getLayer(project, fGeodataSet, sld);
            WorkspaceSync.sync(map, IResource.DEPTH_INFINITE);
            MapTool.addMapLayer(map, layer, ADD_MAP_LAYER_AT.eFront);
        }
        WorkspaceSync.sync(project, IResource.DEPTH_INFINITE);
        return map;
    }
