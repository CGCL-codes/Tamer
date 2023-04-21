    private void commitLimbo() throws VCSException {
        LimboIndex limboIndex = new LimboIndex(source.getPersistenceHelper());
        for (String limboFile : limboIndex.getLimboIndexes()) {
            String fullToLocation = source.getLocalRootDirectory() + limboFile;
            String fullFromLocation = source.getPersistenceHelper().getDataLayout().getLimboDir() + limboFile;
            boolean successfullAdd = false;
            while (!successfullAdd) {
                String limboDirs = limboFile.substring(0, limboFile.lastIndexOf(File.separator));
                File parentPath = new File(fullToLocation.substring(0, fullToLocation.lastIndexOf(File.separator)));
                String topDirThatDoesExistInParentPath = findNewParentDir(parentPath.getAbsolutePath());
                try {
                    File fromPath = new File(fullFromLocation);
                    File toPath = new File(fullToLocation);
                    if (!parentPath.exists()) {
                        parentPath.mkdirs();
                    }
                    FileUtils.copyFile(fromPath, toPath);
                    try {
                        StringTokenizer stringTokenizer = new StringTokenizer(limboDirs, File.separator);
                        String path = source.getLocalRootDirectory();
                        while (stringTokenizer.hasMoreTokens()) {
                            path += (File.separator + stringTokenizer.nextToken());
                            if (isAddRequired(new File(path))) {
                                add(new File(path));
                            }
                        }
                        if (isAddRequired(toPath)) {
                            add(toPath);
                        }
                    } catch (SVNException svnException) {
                        logger.throwing(this.getClass().getName(), "commiting limbo", svnException);
                        throw SVNExceptionConverter.convert(svnException);
                    }
                    if (topDirThatDoesExistInParentPath == null) {
                        commit(new File[] { toPath });
                    } else {
                        commit(new File[] { new File(topDirThatDoesExistInParentPath) });
                    }
                    commit(new File[] { getRootFile() });
                    successfullAdd = true;
                } catch (VCSPathAlreadyExistsInRepositoryException vcsPathAlreadyExistsInRepositoryException) {
                    File artifactDir = new File(source.getPersistenceHelper().getDataLayout().getArtifactsDir());
                    File topDir = new File(topDirThatDoesExistInParentPath);
                    delete(topDir);
                    String artifactsDir = source.getPersistenceHelper().getDataLayout().getArtifactsDir();
                    boolean successfullUpdate = false;
                    while (!successfullUpdate) {
                        if (topDir.getAbsolutePath().equals(artifactsDir)) {
                            update(getRootFile());
                        } else {
                            update(artifactDir);
                        }
                        successfullUpdate = true;
                    }
                } catch (IOException ioException) {
                    logger.severe(ioException.getMessage());
                }
            }
        }
        File limboDir = new File(source.getPersistenceHelper().getDataLayout().getLimboDir());
        if (limboDir.exists()) {
            for (File file : limboDir.listFiles()) {
                if (file.isDirectory()) {
                    FileUtils.deleteDir(file);
                } else {
                    file.delete();
                }
            }
        }
    }
