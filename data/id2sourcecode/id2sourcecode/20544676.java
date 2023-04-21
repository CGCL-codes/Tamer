    private void installPomFile(Model model, File source) throws IOException {
        String artifactId = model.getArtifactId();
        String version = model.getVersion();
        String groupId = model.getGroupId();
        Repository localRepository = resolver.getLocalRepository();
        File pom = localRepository.getMetadataFile(groupId, artifactId, version, model.getPackaging(), artifactId + "-" + version + ".pom");
        System.out.println("Installing POM: " + pom);
        FileUtils.copyFile(source, pom);
    }
