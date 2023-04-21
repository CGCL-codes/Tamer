    private static void addDirectoryFileToZip(DirectoryFile file, ZipOutputStream zipOutStream, String previousPath) {
        log.debug("File " + file.getName() + " previousPath: " + previousPath);
        DirectoryPersister directoryPersister = ManagerRegistry.getDirectoryPersister();
        BufferedInputStream in = new BufferedInputStream(directoryPersister.getInputStreamFromUuid(file.getId()));
        ZipEntry zipEntry = new ZipEntry(previousPath + file.getName());
        try {
            zipOutStream.putNextEntry(zipEntry);
            writeInputStreamIntoOutputStream(in, zipOutStream);
            zipOutStream.closeEntry();
        } catch (IOException e) {
            String errorMessage = "Error adding file " + previousPath + file.getName();
            log.error(errorMessage, e);
            throw new CMSRuntimeException(errorMessage, e);
        }
    }
