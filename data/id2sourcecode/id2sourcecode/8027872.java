    private File getResultDestination(Article article, boolean overwriteFile) throws FileAlreadyExistsException {
        File baseDirectory = new File(ConfigurationResources.getInstance().getOutputDirectory());
        if (!baseDirectory.isDirectory()) {
            throw new AppInfrastructureException("Configuration error: output directory not found");
        }
        File outputFile = new File(baseDirectory, article.getDestinationFileName());
        if (outputFile.isFile()) {
            if (!overwriteFile) {
                throw new FileAlreadyExistsException(outputFile.getName());
            }
        }
        return outputFile;
    }
