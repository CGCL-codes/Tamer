    private boolean getRemoteArtifact(Dependency dep, File destinationFile) {
        boolean fileFound = false;
        List<Object> repositories = new ArrayList<Object>();
        repositories.addAll(getRemoteRepositories());
        repositories.addAll(dep.getRepositories());
        for (Object o : dep.getChain()) {
            repositories.addAll(((Model) o).getRepositories());
        }
        for (Object repository : repositories) {
            Repository remoteRepo = (Repository) repository;
            boolean snapshot = isSnapshot(dep);
            if (snapshot && !remoteRepo.isSnapshots()) {
                continue;
            }
            if (!snapshot && !remoteRepo.isReleases()) {
                continue;
            }
            String url = remoteRepo.getBasedir() + "/" + remoteRepo.getArtifactPath(dep);
            try {
                String version = dep.getVersion();
                if (snapshot) {
                    String filename = "maven-metadata-" + remoteRepo.getId() + ".xml";
                    File localFile = getLocalRepository().getMetadataFile(dep.getGroupId(), dep.getArtifactId(), dep.getVersion(), dep.getType(), "maven-metadata-local.xml");
                    File remoteFile = getLocalRepository().getMetadataFile(dep.getGroupId(), dep.getArtifactId(), dep.getVersion(), dep.getType(), filename);
                    String metadataPath = remoteRepo.getMetadataPath(dep.getGroupId(), dep.getArtifactId(), dep.getVersion(), dep.getType(), "maven-metadata.xml");
                    String metaUrl = remoteRepo.getBasedir() + "/" + metadataPath;
                    log("Downloading " + metaUrl);
                    if (metaUrl.startsWith("file://")) {
                        loadFileLocally(metaUrl, remoteFile);
                    } else {
                        try {
                            HttpUtils.getFile(metaUrl, remoteFile, ignoreErrors, true, proxyHost, proxyPort, proxyUserName, proxyPassword, false);
                        } catch (IOException e) {
                            log("WARNING: remote metadata version not found, using local: " + e.getMessage());
                            remoteFile.delete();
                        }
                    }
                    File file = localFile;
                    if (remoteFile.exists()) {
                        if (!localFile.exists()) {
                            file = remoteFile;
                        } else {
                            RepositoryMetadata localMetadata = RepositoryMetadata.read(localFile);
                            RepositoryMetadata remoteMetadata = RepositoryMetadata.read(remoteFile);
                            if (remoteMetadata.getLastUpdatedUtc() > localMetadata.getLastUpdatedUtc()) {
                                file = remoteFile;
                            } else {
                                file = localFile;
                            }
                        }
                    }
                    if (file.exists()) {
                        log("Using metadata: " + file);
                        RepositoryMetadata metadata = RepositoryMetadata.read(file);
                        if (!file.equals(localFile)) {
                            version = metadata.constructVersion(version);
                        }
                        log("Resolved version: " + version);
                        dep.setResolvedVersion(version);
                        if (!version.endsWith("SNAPSHOT")) {
                            String ver = version.substring(version.lastIndexOf("-", version.lastIndexOf("-") - 1) + 1);
                            String extension = url.substring(url.length() - 4);
                            url = getSnapshotMetadataFile(url, ver + extension);
                        } else if (destinationFile.exists()) {
                            return true;
                        }
                    }
                }
                if (!"pom".equals(dep.getType())) {
                    String name = dep.getArtifactId() + "-" + dep.getResolvedVersion() + ".pom";
                    File file = getLocalRepository().getMetadataFile(dep.getGroupId(), dep.getArtifactId(), dep.getVersion(), dep.getType(), name);
                    file.getParentFile().mkdirs();
                    if (!file.exists() || version.indexOf("SNAPSHOT") >= 0) {
                        String filename = dep.getArtifactId() + "-" + version + ".pom";
                        String metadataPath = remoteRepo.getMetadataPath(dep.getGroupId(), dep.getArtifactId(), dep.getVersion(), dep.getType(), filename);
                        String metaUrl = remoteRepo.getBasedir() + "/" + metadataPath;
                        log("Downloading " + metaUrl);
                        if (metaUrl.startsWith("file://")) {
                            loadFileLocally(metaUrl, file);
                        } else {
                            try {
                                HttpUtils.getFile(metaUrl, file, ignoreErrors, false, proxyHost, proxyPort, proxyUserName, proxyPassword, false);
                            } catch (IOException e) {
                                log("Couldn't find POM - ignoring: " + e.getMessage());
                            }
                        }
                    }
                }
                destinationFile = getLocalRepository().getArtifactFile(dep);
                if (!destinationFile.exists()) {
                    log("Downloading " + url);
                    if (url.startsWith("file://")) {
                        loadFileLocally(url, destinationFile);
                    } else {
                        HttpUtils.getFile(url, destinationFile, ignoreErrors, useTimestamp, proxyHost, proxyPort, proxyUserName, proxyPassword, true);
                        if (dep.getVersion().indexOf("SNAPSHOT") >= 0) {
                            String name = StringUtils.replace(destinationFile.getName(), version, dep.getVersion());
                            FileUtils.copyFile(destinationFile, new File(destinationFile.getParentFile(), name));
                        }
                    }
                }
                fileFound = true;
            } catch (FileNotFoundException e) {
                log("Artifact not found at [" + url + "]");
            } catch (Exception e) {
                log("Error retrieving artifact from [" + url + "]: " + e);
            }
        }
        return fileFound;
    }
