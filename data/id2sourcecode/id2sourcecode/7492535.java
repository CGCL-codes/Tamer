    private static void addDirectoryFolderToZip(ZipOutputStream zipOutStream, DirectoryFolder folder, String previousPath) {
        log.debug("Directory " + folder.getName() + " previousPath: " + previousPath);
        ZipEntry zipEntry = new ZipEntry(previousPath + folder.getName() + '/');
        try {
            zipOutStream.putNextEntry(zipEntry);
            zipOutStream.closeEntry();
            log.debug("Processing item: " + previousPath + folder.getName() + '/');
            addDirectoryItemsToZip(folder, previousPath + folder.getName() + "/", zipOutStream);
        } catch (IOException e) {
            String errorMessage = "Error adding folder " + previousPath + "/" + folder.getName() + " to zip.";
            log.error(errorMessage, e);
            throw new CMSRuntimeException(errorMessage, e);
        }
    }
