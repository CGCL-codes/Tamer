    @Override
    public boolean performFinish() {
        try {
            final File srcFile = new File(m_settings.m_documentFilePath.getText());
            final File destFile = m_settings.m_documentFolder.getFile(m_settings.m_tFileName.getText()).getLocation().toFile();
            FileUtils.copyFile(srcFile, destFile);
            WorkspaceSync.sync(m_settings.m_project, IResource.DEPTH_INFINITE);
            final CommandableWorkspace workspace = m_settings.m_workspace;
            final Feature rootFeature = workspace.getRootFeature();
            final IFeatureSelectionManager selectionManager = KalypsoCorePlugin.getDefault().getSelectionManager();
            final IRelationType relation = (IRelationType) rootFeature.getFeatureType().getProperty(IProjectInfoModel.QN_DOCUMENTS);
            IFeatureType targetFeatureType = null;
            final IGMLSchema schema = workspace.getGMLSchema();
            final DOCUMENT_TYPE type = NewDocumentSettings.DOCUMENT_TYPE.getType(m_settings.m_tFileName.getText());
            switch(type) {
                case notDefined:
                    return true;
                case general:
                    targetFeatureType = schema.getFeatureType(IDocumentMember.QN_TYPE_GENERAL);
                    break;
                case image:
                    targetFeatureType = schema.getFeatureType(IDocumentMember.QN_TYPE_IMAGE);
                    break;
                case pdf:
                    targetFeatureType = schema.getFeatureType(IDocumentMember.QN_TYPE_PDF);
                    break;
                case text:
                    targetFeatureType = schema.getFeatureType(IDocumentMember.QN_TYPE_TEXT);
                    break;
                case spreadsheet:
                    targetFeatureType = schema.getFeatureType(IDocumentMember.QN_TYPE_SPREADSHEET);
                    break;
                case presentation:
                    targetFeatureType = schema.getFeatureType(IDocumentMember.QN_TYPE_PRESENTATION);
                    break;
                case archive:
                    targetFeatureType = schema.getFeatureType(IDocumentMember.QN_TYPE_ARCHIVE);
                    break;
                default:
                    return true;
            }
            final Map<IPropertyType, Object> properties = new HashMap<IPropertyType, Object>();
            properties.put(targetFeatureType.getProperty(IDocumentMember.QN_NAME), m_settings.m_tName.getText());
            properties.put(targetFeatureType.getProperty(IDocumentMember.QN_FILE_NAME), m_settings.m_tFileName.getText());
            properties.put(targetFeatureType.getProperty(IDocumentMember.QN_DESCRIPTION), m_settings.m_tDesc.getText());
            properties.put(targetFeatureType.getProperty(IDocumentMember.QN_DATA_SOURCE), m_settings.m_documentFilePath.getText());
            properties.put(targetFeatureType.getProperty(IDocumentMember.QN_COPYRIGHT), m_settings.m_tCopy.getText());
            properties.put(targetFeatureType.getProperty(IDocumentMember.QN_ORIGIN), m_settings.m_tSupply.getText());
            properties.put(targetFeatureType.getProperty(IDocumentMember.QN_IMPORT_DATE), new XMLGregorianCalendarImpl(new GregorianCalendar()));
            final AddFeatureCommand command = new AddFeatureCommand(workspace, targetFeatureType, rootFeature, relation, -1, properties, selectionManager, -1);
            workspace.postCommand(command);
        } catch (final Exception e) {
            NofdpCorePlugin.getDefault().getLog().log(StatusUtilities.statusFromThrowable(e));
        }
        return true;
    }
