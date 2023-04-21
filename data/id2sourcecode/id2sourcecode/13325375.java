    protected BundleInfo bundle(MavenProject project) throws MojoExecutionException {
        Artifact artifact = project.getArtifact();
        getLog().info("Bundling " + artifact);
        try {
            Map instructions = new LinkedHashMap();
            instructions.put(Analyzer.IMPORT_PACKAGE, wrapImportPackage);
            project.getArtifact().setFile(getFile(artifact));
            File outputFile = getOutputFile(artifact);
            if (project.getArtifact().getFile().equals(outputFile)) {
                return null;
            }
            Analyzer analyzer = getAnalyzer(project, instructions, new Properties(), getClasspath(project));
            Jar osgiJar = new Jar(project.getArtifactId(), project.getArtifact().getFile());
            outputFile.getAbsoluteFile().getParentFile().mkdirs();
            Collection exportedPackages;
            if (isOsgi(osgiJar)) {
                getLog().info("Using existing OSGi bundle for " + project.getGroupId() + ":" + project.getArtifactId() + ":" + project.getVersion());
                String exportHeader = osgiJar.getManifest().getMainAttributes().getValue(Analyzer.EXPORT_PACKAGE);
                exportedPackages = analyzer.parseHeader(exportHeader).keySet();
                FileUtils.copyFile(project.getArtifact().getFile(), outputFile);
            } else {
                exportedPackages = analyzer.getExports().keySet();
                Manifest manifest = analyzer.getJar().getManifest();
                osgiJar.setManifest(manifest);
                osgiJar.write(outputFile);
            }
            BundleInfo bundleInfo = addExportedPackages(project, exportedPackages);
            analyzer.close();
            osgiJar.close();
            return bundleInfo;
        } catch (Exception e) {
            throw new MojoExecutionException("Error generating OSGi bundle for project " + getArtifactKey(project.getArtifact()), e);
        }
    }
