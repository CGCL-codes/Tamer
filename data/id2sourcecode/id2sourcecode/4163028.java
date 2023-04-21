    private void copyGlobalSWCDependencies(MavenProject project, HashMap<String, EclipseDependency> projectDependencies) throws MojoExecutionException {
        for (EclipseDependency dep : projectDependencies.values()) {
            checkGlobalDependency(dep.getArtifactId(), dep.getVersion());
            if (dep.getArtifactId().equals(PLAYERGLOBAL) || dep.getArtifactId().equals(AIRGLOBAL)) {
                File libsDir = new File(project.getBasedir() + "/libs");
                if (!libsDir.exists()) {
                    libsDir.mkdir();
                }
                try {
                    File dependencyFile = new File(libsDir, dep.getArtifactId() + "." + getDependenciesExtension());
                    FileUtils.copyFile(dep.getFile(), dependencyFile);
                    dep.setFile(dependencyFile);
                } catch (IOException e) {
                    throw new MojoExecutionException("Unable to copy " + dep.getArtifactId() + "to libs directory", e);
                }
            }
        }
    }
