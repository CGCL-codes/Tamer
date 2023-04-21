    public void execute() throws MojoExecutionException, MojoFailureException {
        if (this.deployablesDir == null) {
            throw new MojoExecutionException(Messages.getString("xtools.defineDestinationDir"));
        }
        Set<Artifact> artifacts = project.getArtifacts();
        String m2repo = localRepository.getBasedir();
        for (Artifact artifact : artifacts) {
            String artifactScope = artifact.getScope();
            if (artifactScope.equals("compile") || (artifactScope.equals("test"))) {
                String artifactId = artifact.getArtifactId();
                String groupId = artifact.getGroupId();
                String versionId = artifact.getVersion();
                String artifactName = artifactId + "-" + versionId + ".jar";
                File srcFile = new File(m2repo + File.separator + StringUtils.replace(groupId, ".", File.separator) + File.separator + artifactId + File.separator + versionId + File.separator + artifactName);
                String srcFileName = Utils.getCanonicalPath(srcFile);
                File destFile = new File(deployablesDir + File.separator + WEBAPP_LIB_PATH + File.separator + artifactName);
                String destFileName = Utils.getCanonicalPath(destFile);
                if (new File(destFileName).exists() == true) {
                    getLog().info(Messages.getString("updateLibs.skippingExistentArtifact", artifactName));
                } else {
                    try {
                        getLog().info(Messages.getString("updateLibs.copying", artifactName));
                        FileUtils.copyFile(new File(srcFileName), new File(destFileName));
                    } catch (IOException e) {
                        getLog().info(Messages.getString("xtools.mojoException", e.getMessage()));
                    }
                }
            }
        }
    }
