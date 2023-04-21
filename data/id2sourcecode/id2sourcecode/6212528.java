    public void buildWebapp(MavenProject project, File webappDirectory) throws MojoExecutionException, IOException {
        getLog().info("Assembling webapp " + project.getArtifactId() + " in " + webappDirectory);
        File libDirectory = new File(webappDirectory, WEB_INF + "/lib");
        File tldDirectory = new File(webappDirectory, WEB_INF + "/tld");
        File webappClassesDirectory = new File(webappDirectory, WEB_INF + "/classes");
        if (getClassesDirectory().exists() && (!getClassesDirectory().equals(webappClassesDirectory))) {
            FileUtils.copyDirectoryStructure(getClassesDirectory(), webappClassesDirectory);
        }
        Set artifacts = project.getArtifacts();
        AndArtifactFilter filter = new AndArtifactFilter();
        filter.add(new ScopeArtifactFilter(Artifact.SCOPE_RUNTIME));
        if (!warDependencyIncludes.isEmpty()) {
            List includes = new ArrayList();
            includes.addAll(warDependencyIncludes);
            filter.add(new IncludesArtifactFilter(includes));
        }
        if (!warDependencyExcludes.isEmpty()) {
            List excludes = new ArrayList();
            excludes.addAll(warDependencyExcludes);
            filter.add(new ExcludesArtifactFilter(excludes));
        }
        List dependentWarDirectories = new ArrayList();
        for (Iterator iter = artifacts.iterator(); iter.hasNext(); ) {
            Artifact artifact = (Artifact) iter.next();
            if (!artifact.isOptional() && filter.include(artifact)) {
                String type = artifact.getType();
                if ("tld".equals(type)) {
                    FileUtils.copyFileToDirectory(artifact.getFile(), tldDirectory);
                } else if ("jar".equals(type) || "ejb".equals(type) || "ejb-client".equals(type)) {
                    FileUtils.copyFileToDirectory(artifact.getFile(), libDirectory);
                } else if ("war".equals(type)) {
                    dependentWarDirectories.add(unpackWarToTempDirectory(artifact));
                } else {
                    getLog().debug("Skipping artifact of type " + type + " for WEB-INF/lib");
                }
            }
        }
        if (dependentWarDirectories.size() > 0) {
            getLog().info("Overlaying " + dependentWarDirectories.size() + " war(s).");
            for (Iterator iter = dependentWarDirectories.iterator(); iter.hasNext(); ) {
                copyDependentWarContents((File) iter.next(), webappDirectory);
            }
        }
    }
