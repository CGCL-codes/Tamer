    private void prepare(final File directory, final IProgressMonitor monitor, final Category cat) throws InvocationTargetException, InterruptedException {
        ModalContext.checkCanceled(monitor);
        File[] listFiles = directory.listFiles();
        monitor.beginTask(NLS.bind(Messages.ImportImagesFromDiscRunnable_SearchTroughFolder, directory.getName()), listFiles.length);
        for (File file : listFiles) {
            Path path = new Path(file.getAbsolutePath());
            if (isValidImageExtension(path)) {
                LoadFileToTmpFromPathRunnable copy2Tmp = new LoadFileToTmpFromPathRunnable();
                copy2Tmp.setFilePath(file.getAbsolutePath());
                copy2Tmp.run(new SubProgressMonitor(monitor, IProgressMonitor.UNKNOWN));
                monitor.setTaskName(NLS.bind(Messages.ImportImagesFromDiscRunnable_AddingImage, path.lastSegment()));
                InformationUnit createNewObject = InformationStructureEdit.newSession(ImagePlugin.TYPE_ID).newInformationUnit();
                LoadImageRunnable loadImageRunnable = new LoadImageRunnable(true);
                loadImageRunnable.setImagePath(copy2Tmp.getTmpFile().getLocation().toOSString());
                loadImageRunnable.setDomain(this.editingDomain);
                loadImageRunnable.setImageNode(createNewObject);
                loadImageRunnable.run(new SubProgressMonitor(monitor, IProgressMonitor.UNKNOWN));
                createNewObject.setLabel(file.getName());
                CompoundCommand createInfotype = CommandFactory.CREATE_INFOTYPE(createNewObject, cat, monitor);
                createInfotype.append(CommandFactory.addFileToInfoUnit(copy2Tmp.getTmpFile(), createNewObject, this.editingDomain));
                createInfotype.execute();
                this.editingDomain.getCommandStack().execute(createInfotype);
                this.editingDomain.getCommandStack().flush();
                monitor.worked(1);
            }
            if (file.isDirectory() && file.list(new FilenameFilter() {

                public boolean accept(final File dir, final String name) {
                    return isValidImageExtension(new Path(name)) || dir.isDirectory();
                }
            }).length > 0) {
                if (this.obj.isCreateFolderStructure()) {
                    Category createCategory = InfomngmntFactory.eINSTANCE.createCategory();
                    createCategory.setId(IdFactory.createNewId(null));
                    createCategory.setLabel(file.getName());
                    Command command = CommandFactory.CREATE_CATEGORY(cat, createCategory, this.editingDomain);
                    this.editingDomain.getCommandStack().execute(command);
                    prepare(file, monitor, createCategory);
                } else {
                    prepare(file, monitor, cat);
                }
            }
            if (listFiles.length == 0) {
                Command deleteCategory = CommandFactory.DELETE_CATEGORY(cat, this.editingDomain);
                deleteCategory.execute();
            }
        }
    }
