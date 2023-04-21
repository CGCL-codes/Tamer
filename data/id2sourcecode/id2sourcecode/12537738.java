    @Override
    public boolean saveModel(IArchimateModel model) throws IOException {
        if (model.getFile() == null) {
            File file = askSaveModel();
            if (file == null) {
                return false;
            }
            model.setFile(file);
        }
        File file = model.getFile();
        if (file.exists()) {
            FileUtils.copyFile(file, new File(model.getFile().getAbsolutePath() + ".bak"), false);
        }
        model.setVersion(ModelVersion.VERSION);
        IArchiveManager archiveManager = (IArchiveManager) model.getAdapter(IArchiveManager.class);
        archiveManager.saveModel();
        CommandStack stack = (CommandStack) model.getAdapter(CommandStack.class);
        stack.markSaveLocation();
        firePropertyChange(model, COMMAND_STACK_CHANGED, true, false);
        markDiagramModelsAsSaved(model);
        firePropertyChange(this, PROPERTY_MODEL_SAVED, null, model);
        return true;
    }
