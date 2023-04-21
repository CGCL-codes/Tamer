    protected static void zipSingleFile(FileWrapper file, String filename, ZipOutputStream zipStream, SubMonitor progress) throws IOException, SarosCancellationException {
        try {
            if (progress.isCanceled()) {
                throw new SarosCancellationException();
            }
            progress.beginTask("Compressing: " + filename, 1);
            log.debug("Compress file: " + filename);
            if (file == null || !file.exists()) {
                throw new IllegalArgumentException("The file to zip does not exist: " + filename);
            }
            zipStream.putNextEntry(new ZipEntry(filename));
            writeFileToStream(file, filename, zipStream);
            zipStream.closeEntry();
        } finally {
            progress.done();
        }
    }
