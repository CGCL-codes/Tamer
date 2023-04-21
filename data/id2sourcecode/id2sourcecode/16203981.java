    protected void doExecute() throws Exception {
        System.out.println("\n*************************\tBegin doExecute()\t*************************\n");
        File webINF = new File(webappDirectory, "WEB-INF");
        FileUtils.copyDirectory(classesDirectory, webINF);
        Set artifacts = project.getArtifacts();
        File libDir = new File(webINF, "lib");
        for (Iterator iter = artifacts.iterator(); iter.hasNext(); ) {
            Artifact artifact = (Artifact) iter.next();
            FileUtils.copyFileToDirectory(artifact.getFile(), libDir);
        }
        MavenArchiver archiver = new MavenArchiver();
        WarArchiver warArchiver = new WarArchiver();
        archiver.setArchiver(warArchiver);
        archiver.setOutputFile(warFile);
        warArchiver.addDirectory(webappDirectory);
        warArchiver.setWebxml(new File(webappDirectory, "WEB-INF/web.xml"));
        archiver.createArchive(project, new MavenArchiveConfiguration());
        project.getArtifact().setFile(warFile);
        System.out.println("\n\n*************************\tEnd doExecute()\t*************************\n");
    }
