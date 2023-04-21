    private void preloadFromRepository(String repoUrl, PathItemBean[] artifactArray) throws IOException {
        for (PathItemBean a : artifactArray) {
            final String uri = gavUri(a.getGroupId(), a.getArtifactId(), a.getVersion(), a.getClassifier(), a.getType());
            final File destFile = new File(mavenRepo, uri);
            File foundLocally = lookupLocally(a);
            if (foundLocally == null) {
                if (repoUrl == null) {
                    throw new FileNotFoundException(destFile.getAbsolutePath());
                }
                final File destTmp = downloadArtifact(repoUrl, a);
                checkFile(destTmp, a.getDigestArray());
                destFile.delete();
                destTmp.renameTo(destFile);
                foundLocally = destFile;
            }
            foundLocally = foundLocally.getCanonicalFile();
            if (!foundLocally.equals(destFile.getCanonicalFile())) {
                logger.log(CAT.copying(foundLocally, destFile));
                FileUtils.copyFile(foundLocally, destFile);
            }
        }
    }
