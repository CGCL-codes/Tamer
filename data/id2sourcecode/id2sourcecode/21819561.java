    private void prepareTarget() throws MojoExecutionException {
        String artifactId = getArchetypeProperty("artifactId");
        File pomDirectory = new File(targetDirectory, artifactId);
        m_pomFile = new File(pomDirectory, "pom.xml");
        if (m_pomFile.exists()) {
            if (overwrite) {
                m_pomFile.delete();
            } else {
                throw new MojoExecutionException("Project already exists, use -Doverwrite or -o to replace it");
            }
        }
        m_tempFiles = new FileSet();
        m_tempFiles.setDirectory(pomDirectory.getAbsolutePath());
        if (pomDirectory.exists()) {
            preserveExistingFiles(pomDirectory);
        } else {
            pomDirectory.mkdirs();
        }
        if (null != m_modulesPom) {
            setArchetypeProperty("isMultiModuleProject", "true");
            try {
                m_modulesPom.addModule(pomDirectory.getName(), true);
                m_modulesPom.write();
            } catch (IOException e) {
                getLog().warn("Unable to attach POM to existing project");
            }
        }
    }
