    private Pom repairBundleImports(PaxScript script, MavenProject project) {
        Pom pom;
        try {
            File tempFile = File.createTempFile("pom", ".xml", m_tempdir);
            FileUtils.copyFile(project.getFile(), tempFile);
            pom = PomUtils.readPom(tempFile);
            tempFile.deleteOnExit();
        } catch (IOException e) {
            pom = null;
        }
        for (Iterator i = project.getDependencies().iterator(); i.hasNext(); ) {
            Dependency dependency = (Dependency) i.next();
            String bundleId = dependency.getGroupId() + ':' + dependency.getArtifactId();
            String bundleName = (String) m_bundleNameMap.get(bundleId);
            if (null != bundleName) {
                PaxCommandBuilder command;
                command = script.call(PaxScript.IMPORT_BUNDLE);
                command.option('a', bundleName);
                command.flag('o');
                setTargetDirectory(command, project.getBasedir());
                if (null != pom) {
                    pom.removeDependency(dependency);
                }
            }
        }
        try {
            if (null != pom) {
                pom.write();
            }
            return pom;
        } catch (IOException e) {
            return null;
        }
    }
