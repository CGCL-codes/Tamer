    private void checkOutputFile(File outputFile, boolean force) throws ExtractionException {
        if (outputFile.exists() && !force) {
            error("Output file '" + outputFile + "' already exists.  " + "(Use --force to overwrite it.)");
        }
    }
