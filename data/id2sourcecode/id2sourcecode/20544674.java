    private void run(String goal) throws Exception {
        Date fullStart = new Date();
        String basedir = System.getProperty("user.dir");
        File pom = new File(basedir, "pom.xml");
        Model reader = readModel(pom, true);
        File jar = buildProject(reader);
        if ("install".equals(goal)) {
            install(reader, pom, jar);
        }
        for (Iterator i = reader.getAllDependencies().iterator(); i.hasNext(); ) {
            Dependency dep = (Dependency) i.next();
            FileUtils.copyFileToDirectory(resolver.getArtifactFile(dep), jar.getParentFile());
        }
        stats(fullStart, new Date());
    }
