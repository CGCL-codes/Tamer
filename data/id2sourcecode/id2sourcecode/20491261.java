    private void scalaHack(File scalaModuleDirectory) throws IOException {
        Set<?> projectArtifacts = project.getArtifacts();
        for (Iterator<?> iter = projectArtifacts.iterator(); iter.hasNext(); ) {
            Artifact artifact = (Artifact) iter.next();
            if ("org.scala-lang".equals(artifact.getGroupId()) && ("scala-compiler".equals(artifact.getArtifactId()) || "scala-library".equals(artifact.getArtifactId())) && "jar".equals(artifact.getType())) {
                File jarFile = artifact.getFile();
                FileUtils.copyFileIfModified(jarFile, new File(scalaModuleDirectory, "lib/" + artifact.getArtifactId() + ".jar"));
            }
        }
    }
