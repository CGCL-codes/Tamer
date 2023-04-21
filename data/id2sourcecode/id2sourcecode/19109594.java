    private File copyDependencies(final File root) throws MojoExecutionException {
        getLog().info("copy runtime dependencies");
        final File libDirectory = new File(root, "lib");
        if (!libDirectory.mkdirs()) {
            throw new MojoExecutionException("Cannot create libraries directory " + libDirectory.getName());
        }
        try {
            final Set<Artifact> artifacts = new HashSet<Artifact>();
            final ArtifactFilter artifactFilter = new ScopeArtifactFilter(null);
            final DependencyNode rootNode = treeBuilder.buildDependencyTree(project, localRepository, artifactFactory, artifactMetadataSource, artifactFilter, artifactCollector);
            for (final Iterator<?> iterator = rootNode.getChildren().iterator(); iterator.hasNext(); ) {
                final DependencyNode child = (DependencyNode) iterator.next();
                collect(child, artifacts);
            }
            for (final Artifact artifact : artifacts) {
                try {
                    resolver.resolve(artifact, remoteRepositories, localRepository);
                } catch (final ArtifactResolutionException e) {
                    throw new MojoExecutionException("Unable to resolve " + artifact, e);
                } catch (final ArtifactNotFoundException e) {
                    throw new MojoExecutionException("Unable to resolve " + artifact, e);
                }
                FileUtils.copyFileToDirectory(artifact.getFile(), libDirectory);
            }
        } catch (final DependencyTreeBuilderException e) {
            throw new MojoExecutionException("Error analysing dependency", e);
        } catch (final IOException e) {
            throw new MojoExecutionException("Error copying libs", e);
        }
        return libDirectory;
    }
