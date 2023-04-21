    private static List<String> queryVersionsFromRepositoryDirectory(final String thisRepoUrl, final String thisGroupId, final String thisArtifactId) throws MalformedURLException, IOException {
        final String url = Maven.makeFolderUrl(thisRepoUrl, thisGroupId, thisArtifactId);
        return new MavenRepositoryDirectoryScanner().scan(new InputStreamReader(new URL(url).openStream()));
    }
