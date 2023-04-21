    void addFileToZip(File file, ZipOutputStream zipOutStream) throws FileNotFoundException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
        ZipEntry zipEntry = new ZipEntry(file.getName());
        try {
            try {
                zipOutStream.putNextEntry(zipEntry);
            } catch (ZipException e) {
                log.warn("File " + file.getName() + " is already at the zip. Ignored");
                zipOutStream.closeEntry();
                return;
            }
            writeInputStreamIntoOutputStream(in, zipOutStream);
        } catch (IOException e) {
            String errorMessage = "Error adding file " + file.getName();
            log.error(errorMessage, e);
            throw new CMSRuntimeException(errorMessage, e);
        }
    }
