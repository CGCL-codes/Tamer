    private File createSourceTempFile(DigitalObject sourceObject, String tempFileBaseName) {
        final File sourceTempFile = FileUtils.getTempFile(tempFileBaseName, "source");
        FileUtils.writeInputStreamToFile(sourceObject.getContent().read(), sourceTempFile);
        return sourceTempFile;
    }
