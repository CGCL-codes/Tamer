    private void runInternal(IProgressMonitor monitor) {
        try {
            List<FileGenerator> fileGeneratorList = new ArrayList<FileGenerator>();
            ConnectionInformation connectionInformation = mappingData.getConnectionInformation();
            GenerationInformation generationInformation = mappingData.getGenerationInformation();
            List<Entity> entityList = mappingData.getEntityList();
            fileGeneratorList.add(new PomFileGenerator(generationInformation));
            fileGeneratorList.add(new WebXmlFileGenerator(generationInformation));
            fileGeneratorList.add(new ServletXmlFileGenerator(generationInformation, entityList));
            fileGeneratorList.add(new SqlMapConfigFileGenerator(generationInformation, entityList));
            fileGeneratorList.add(new Log4jDtdFileGenerator(generationInformation));
            fileGeneratorList.add(new Log4jXmlFileGenerator(generationInformation));
            fileGeneratorList.add(new MessagePropertiesFileGenerator(generationInformation));
            fileGeneratorList.add(new MessageKoPropertiesFileGenerator(generationInformation));
            fileGeneratorList.add(new JdbcPropertiesFileGenerator(generationInformation, connectionInformation));
            fileGeneratorList.add(new ResourcePropertiesFileGenerator(generationInformation));
            fileGeneratorList.add(new JdbcDriverFileGenerator(generationInformation, connectionInformation));
            fileGeneratorList.add(new BaseCriteriaFileGenerator(generationInformation));
            fileGeneratorList.add(new BaseServiceFileGenerator(generationInformation));
            fileGeneratorList.add(new PagingFileGenerator(generationInformation));
            fileGeneratorList.add(new UploadSaveDirectoryGenerator(generationInformation));
            fileGeneratorList.add(new AttachFileFileGenerator(generationInformation));
            fileGeneratorList.add(new AttachFilePersisterFileGenerator(generationInformation));
            fileGeneratorList.add(new AttachFilePropertyEditorFileGenerator(generationInformation));
            fileGeneratorList.add(new FilenameGeneratorFileGenerator(generationInformation));
            fileGeneratorList.add(new UUIDFilenameGeneratorFileGenerator(generationInformation));
            fileGeneratorList.add(new DefaultMethodInvocationLoggerFileGenerator(generationInformation));
            fileGeneratorList.add(new MethodInvocationInfoInterceptorFileGenerator(generationInformation));
            fileGeneratorList.add(new MethodInvocationLoggerFileGenerator(generationInformation));
            fileGeneratorList.add(new MethodInvocationLoggingAdviceFileGenerator(generationInformation));
            fileGeneratorList.add(new EntityFileGenerator(generationInformation));
            fileGeneratorList.add(new CriteriaFileGenerator(generationInformation));
            fileGeneratorList.add(new ValidatorFileGenerator(generationInformation));
            fileGeneratorList.add(new DaoInterfaceFileGenerator(generationInformation));
            fileGeneratorList.add(new DaoClassFileGenerator(generationInformation));
            fileGeneratorList.add(new SqlMapFileGenerator(generationInformation));
            fileGeneratorList.add(new ServiceInterfaceFileGenerator(generationInformation));
            fileGeneratorList.add(new ServiceClassFileGenerator(generationInformation));
            fileGeneratorList.add(new ControllerFileGenerator(generationInformation));
            fileGeneratorList.add(new FormControllerFileGenerator(generationInformation));
            fileGeneratorList.add(new StyleFileGenerator(generationInformation));
            fileGeneratorList.add(new TagsFileGenerator(generationInformation));
            fileGeneratorList.add(new DefaultPagingFileGenerator(generationInformation));
            fileGeneratorList.add(new IndexFileGenerator(generationInformation, entityList));
            fileGeneratorList.add(new ListPageFileGenerator(generationInformation));
            fileGeneratorList.add(new DetailPageFileGenerator(generationInformation));
            fileGeneratorList.add(new WritePageFileGenerator(generationInformation));
            fileGeneratorList.add(new EditPageFileGenerator(generationInformation));
            monitor.beginTask("Generate soure files", (entityList != null && entityList.size() > 0) ? entityList.size() * fileGeneratorList.size() : 0);
            if (entityList != null && entityList.size() > 0) {
                boolean overwriteYesToAll = false;
                boolean overwriteNoToAll = false;
                List<File> generatedFileList = new ArrayList<File>();
                for (FileGenerator fileGenerator : fileGeneratorList) {
                    fileGenerator.generateDirectory();
                    for (Entity entity : entityList) {
                        monitor.subTask(fileGenerator.getFile(entity).getPath());
                        if (entity.isCreate()) {
                            try {
                                if (fileGenerator.existFile(entity)) {
                                    if (overwriteNoToAll) {
                                        continue;
                                    }
                                    if (!overwriteYesToAll) {
                                        boolean overwrite = false;
                                        MessageDialog overwriteDialog = new MessageDialog(editor.getSite().getShell(), "Question", null, "The file '" + fileGenerator.getFile(entity) + "' already exists. Do you want to replace the existing file?", MessageDialog.WARNING, new String[] { IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL, IDialogConstants.YES_TO_ALL_LABEL, IDialogConstants.NO_TO_ALL_LABEL }, 1);
                                        int value = overwriteDialog.open();
                                        switch(value) {
                                            case 0:
                                                overwrite = true;
                                                break;
                                            case 1:
                                                overwrite = false;
                                                break;
                                            case 2:
                                                overwrite = true;
                                                overwriteYesToAll = true;
                                                break;
                                            case 3:
                                                overwrite = false;
                                                overwriteNoToAll = true;
                                                break;
                                            default:
                                                overwriteNoToAll = true;
                                                break;
                                        }
                                        if (!overwrite) {
                                            continue;
                                        }
                                    }
                                }
                                entity.setPackageName(generationInformation.getPackageName());
                                File file = fileGenerator.generate(entity);
                                if (file != null) {
                                    generatedFileList.add(file);
                                }
                            } catch (Exception e) {
                                MessageDialog.openError(editor.getSite().getShell(), "Error - generate", e.getMessage());
                            }
                        }
                        monitor.worked(1);
                    }
                }
                printGeneratedFileList(generatedFileList);
            }
            RefreshAction refreshAction = new RefreshAction(editor.getEditorSite());
            refreshAction.refreshAll();
            monitor.done();
        } catch (Exception e) {
            MessageDialog.openError(editor.getSite().getShell(), "Generate file error", e.getMessage());
        } finally {
        }
    }
