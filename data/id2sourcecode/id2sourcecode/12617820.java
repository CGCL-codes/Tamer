    static File resolveOutputRootFolder(final String outputPath) {
        final File outputFile = new File(outputPath);
        if (outputFile.exists()) {
            if (outputFile.isFile()) throw new IllegalStateException("Output file path is an existing file: " + outputPath);
        } else if (!outputFile.mkdirs()) throw new IllegalStateException("Failed to create output root folder: " + outputPath);
        if ((!outputFile.canRead()) || (!outputFile.canWrite())) throw new IllegalStateException("Cannot access read/write output folder: " + outputPath);
        return outputFile;
    }
