    private void doInstall(Set<OutputArtifact> outputArtifacts, File targetDirectory) throws IOException, InterruptedException {
        logger.log(CAT.installingTo(targetDirectory));
        final String prefix = new File(project.getBuild().getDirectory()).getAbsolutePath() + File.separator;
        for (OutputArtifact artifact : outputArtifacts) {
            final File src = artifact.getFile();
            final String destName = String.format("%s-%s%s.%s", project.getArtifactId(), project.getVersion(), artifact.getClassifier() == null ? "" : "-" + artifact.getClassifier(), artifact.getExtension());
            final File dest = new File(targetDirectory, destName);
            boolean canSymlink = src.getAbsolutePath().startsWith(prefix);
            if (symlink && canSymlink) {
                logger.log(CAT.linking(destName, src));
                FileUtils.rename(src, dest);
                symlink(src, dest);
            } else {
                logger.log(CAT.copying(destName, src));
                FileUtils.copyFile(src, dest);
            }
        }
    }
