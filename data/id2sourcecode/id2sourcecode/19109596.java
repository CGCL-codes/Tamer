    private void copyMainArtifact(final File libDirectory) throws MojoExecutionException {
        try {
            FileUtils.copyFileToDirectory(project.getArtifact().getFile(), libDirectory);
        } catch (final IOException e) {
            throw new MojoExecutionException("Error copying project artifact ", e);
        }
    }
