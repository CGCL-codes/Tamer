    public final void execute() throws MojoExecutionException, MojoFailureException {
        final File attachment = new File(this.getSessionDirectory(), ArtifactUtils.versionlessKey(this.getMavenProject().getArtifact()).hashCode() + "-" + this.getArtifactClassifier() + "-" + this.getMavenSession().getStartTime().getTime() + "." + this.getArtifactType());
        try {
            if (this.isVerbose() && this.getLog().isInfoEnabled()) {
                this.getLog().info(LOG_PREFIX + Messages.getMessage("separator"));
                this.getLog().info(LOG_PREFIX + Messages.getMessage("title"));
            }
            if (MojoDescriptor.MULTI_PASS_EXEC_STRATEGY.equals(this.getExecutionStrategy()) || !attachment.exists()) {
                if (this.isVerbose() && this.getLog().isInfoEnabled()) {
                    this.getLog().info(LOG_PREFIX + Messages.getMessage("separator"));
                    this.getLog().info(LOG_PREFIX + Messages.getMessage("processingProject", TOOLNAME, this.getMavenProject().getName() == null ? this.getMavenProject().getArtifactId() : this.getMavenProject().getName()));
                }
                if (this.getArtifactFile().isFile()) {
                    if (attachment.exists() && !attachment.delete()) {
                        this.getLog().warn(LOG_PREFIX + Messages.getMessage("failedDeletingFile", attachment.getAbsolutePath()));
                    }
                    if (!attachment.getParentFile().exists() && !attachment.getParentFile().mkdirs()) {
                        throw new MojoExecutionException(Messages.getMessage("failedCreatingDirectory", attachment.getParentFile().getAbsolutePath()));
                    }
                    FileUtils.copyFile(this.getArtifactFile(), attachment);
                    this.getMavenProjectHelper().attachArtifact(this.getMavenProject(), this.getArtifactType(), this.getArtifactClassifier(), attachment);
                    if (this.isVerbose() && this.getLog().isInfoEnabled()) {
                        this.getLog().info(LOG_PREFIX + Messages.getMessage("creatingAttachment", this.getArtifactFile().getAbsolutePath(), this.getArtifactClassifier(), this.getArtifactType()));
                        this.getLog().info(LOG_PREFIX + Messages.getMessage("toolSuccess", TOOLNAME));
                    }
                } else if (this.getLog().isWarnEnabled()) {
                    this.getLog().warn(LOG_PREFIX + Messages.getMessage("artifactFileNotFound", this.getArtifactFile().getAbsolutePath()));
                }
            } else if (this.isVerbose() && this.getLog().isInfoEnabled()) {
                this.getLog().info(LOG_PREFIX + Messages.getMessage("executionSuppressed", this.getExecutionStrategy()));
            }
        } catch (final IOException e) {
            final String message = Messages.getMessage(e);
            throw new MojoExecutionException(Messages.getMessage("failedCopying", this.getArtifactFile().getAbsolutePath(), attachment.getAbsolutePath(), message != null ? message : ""), e);
        } finally {
            if (this.isVerbose() && this.getLog().isInfoEnabled()) {
                this.getLog().info(LOG_PREFIX + Messages.getMessage("separator"));
            }
        }
    }
