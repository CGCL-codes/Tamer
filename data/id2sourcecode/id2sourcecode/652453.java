    public void uploadImage(File[] files) throws IOException {
        for (File file : files) {
            FileUtils.copyFile(file.toURI().toURL(), new File(AgentApi.getStorage() + "/" + file.getName()));
        }
    }
