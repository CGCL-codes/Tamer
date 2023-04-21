    public void execute() throws MojoExecutionException, MojoFailureException {
        if (puXMLFile == null || !FileUtils.fileExists(puXMLFile)) {
            throw new MojoExecutionException("missing pu.xml file");
        }
        if (warFile == null || !FileUtils.fileExists(warFile)) {
            throw new MojoExecutionException("missing war file");
        }
        try {
            String puTargetDir = serverDirectory + File.separator + "META-INF" + File.separator + "spring";
            String libTargetDir = serverDirectory + File.separator + "lib";
            FileUtils.mkdir(serverDirectory);
            FileUtils.mkdir(puTargetDir);
            FileUtils.mkdir(libTargetDir);
            FileUtils.copyFileToDirectory(new File(puXMLFile), new File(puTargetDir));
            FileUtils.copyFileToDirectory(new File(warFile), new File(serverDirectory));
            for (Iterator artifactIterator = pluginArtifacts.iterator(); artifactIterator.hasNext(); ) {
                Artifact artifact = (Artifact) artifactIterator.next();
                if (artifact.getGroupId().equals("org.mortbay.jetty")) {
                    FileUtils.copyFileToDirectory(artifact.getFile().getPath(), libTargetDir);
                }
            }
            jarArchiver.addDirectory(new File(serverDirectory));
            jarArchiver.setDestFile(new File(artifactName));
            jarArchiver.createArchive();
        } catch (IOException ioe) {
            throw new MojoExecutionException("unable to assemble", ioe);
        } catch (ArchiverException ae) {
            throw new MojoExecutionException("unable to assembly jar", ae);
        }
    }
