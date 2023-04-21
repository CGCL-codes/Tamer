    private void processArtifacts(File file) {
        if (!file.exists()) {
            return;
        }
        if (!artifactsDestDir.exists()) {
            artifactsDestDir.mkdirs();
        }
        for (File artifactFile : file.listFiles()) {
            if (artifactFile.isDirectory()) {
                File artifactFileDir = new File(artifactsDestDir + File.separator + artifactFile.getName());
                artifactFileDir.mkdir();
                processArtifacts(artifactFile);
            } else {
                File destinationFile = new File(artifactsDestDir + File.separator + artifactFile.getParentFile().getName() + File.separator + artifactFile.getName());
                try {
                    FileUtils.copyFile(artifactFile, destinationFile);
                    artifacts.put(artifactFile.getName(), File.separator + artifactFile.getParentFile().getName() + File.separator + artifactFile.getName());
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "Problem copying artifact: " + destinationFile, e);
                }
            }
        }
    }
