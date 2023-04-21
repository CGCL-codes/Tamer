    protected void doExecute() throws Exception {
        log.info("Project :: " + project.getArtifactId());
        if (!"ear".equals(project.getPackaging())) {
            log.info("Not executing on non-EAR project");
            return;
        }
        final File earDir = new File(outputDirectory, project.getBuild().getFinalName());
        if (!earDir.exists() || !earDir.isDirectory()) {
            throw new MojoExecutionException("Invalid Ear build directory: " + earDir);
        }
        transformedEarDir = new File(outputDirectory, baseName + "-" + classifier);
        FileUtils.copyDirectoryStructure(earDir, transformedEarDir);
        transformedWarFile = new File(transformedWarDir, warname + "-" + project.getVersion() + "-" + classifier + ".war");
        FileUtils.copyFileToDirectory(transformedWarFile, transformedEarDir);
        FileUtils.forceDelete(new File(transformedEarDir, warname + "-" + project.getVersion() + ".war"));
        super.doExecute();
        final File outEar = new File(outputDirectory, baseName + "-" + classifier + ".ear");
        final MavenArchiveConfiguration archive = new MavenArchiveConfiguration();
        archive.setAddMavenDescriptor(true);
        final MavenArchiver archiver = new MavenArchiver();
        archiver.setArchiver(earArchiver);
        archiver.setOutputFile(outEar);
        earArchiver.addDirectory(transformedEarDir);
        earArchiver.setAppxml(new File(transformedEarDir, "META-INF/application.xml"));
        archiver.createArchive(project, archive);
        if (attach) {
            projectHelper.attachArtifact(project, "ear", classifier, outEar);
        }
    }
