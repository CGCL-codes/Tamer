    private void convertDocument(OfficeDocumentConverter converter, File inputFile, File outputFile) throws IOException {
        String inputFileExtension = FilenameUtils.getExtension(inputFile.getName());
        String outputFileExtension = FilenameUtils.getExtension(outputFile.getName());
        boolean sameExtension = false;
        if ((inputFileExtension != null) && (outputFileExtension != null)) {
            if (inputFileExtension.equals(outputFileExtension)) {
                sameExtension = true;
            }
        }
        if (sameExtension) {
            if (isVerboseEnabled()) {
                verbose("input and output file have same extension - copying " + inputFile + " to " + outputFile);
            }
            FileUtils.copyFile(inputFile, outputFile);
        } else {
            if (isVerboseEnabled()) {
                verbose("converting " + inputFile + " to " + outputFile);
            }
            converter.convert(inputFile, outputFile);
        }
    }
