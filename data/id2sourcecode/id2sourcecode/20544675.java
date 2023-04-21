    private void install(Model model, File pom, File jar) throws Exception {
        String artifactId = model.getArtifactId();
        String version = model.getVersion();
        String groupId = model.getGroupId();
        String type = model.getPackaging();
        Repository localRepository = resolver.getLocalRepository();
        File file = localRepository.getArtifactFile(new Dependency(groupId, artifactId, version, type, Collections.EMPTY_LIST));
        System.out.println("Installing: " + file);
        FileUtils.copyFile(jar, file);
        installPomFile(model, pom);
        RepositoryMetadata metadata = new RepositoryMetadata();
        metadata.setReleaseVersion(version);
        metadata.setLatestVersion(version);
        file = localRepository.getMetadataFile(groupId, artifactId, null, type, "maven-metadata-local.xml");
        metadata.write(file);
        metadata = new RepositoryMetadata();
        metadata.setLocalCopy(true);
        metadata.setLastUpdated(getCurrentUtcDate());
        file = localRepository.getMetadataFile(groupId, artifactId, version, type, "maven-metadata-local.xml");
        metadata.write(file);
    }
