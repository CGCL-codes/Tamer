    @Override
    public void processModel(MetaModel model, ValidationResult messages) {
        rootDirectory = new File(getContext().getProperty(NetCartridgeConstants.WPF_ROOT_FOLDER_KEY));
        if (!rootDirectory.exists()) {
            rootDirectory.mkdirs();
        }
        mockRootDirectory = new File(getContext().getProperty(NetCartridgeConstants.WPF_ROOT_FOLDER_KEY) + MOCK_DIRECTORY);
        if (!mockRootDirectory.exists()) {
            mockRootDirectory.mkdirs();
        }
        File skinDirectory = new File(rootDirectory.getPath() + THEME_DIRECTORY);
        if (!skinDirectory.exists()) {
            skinDirectory.mkdirs();
        }
        File skinFile = new File(skinDirectory.getPath() + "/skin-default.xaml");
        if (!skinFile.exists()) {
            try {
                StringBuilder buffer = new StringBuilder();
                buffer.append(getConfiguration().getProperty(RESSOURCE_FOLDER));
                if (getPlatform() != null) {
                    buffer.append(Platform.getPath(getPlatform()) + "/");
                }
                buffer.append(getPrefix()).append("/").append("skin-default.xaml");
                File skinFileToCoyp = new File(buffer.toString());
                FileUtils.copyFileToDirectory(skinFileToCoyp, skinDirectory);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.processModel(model, messages);
    }
