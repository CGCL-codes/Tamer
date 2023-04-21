    void writeFilesToZip(List<String> filesEntries, String basePath, DirectoryFolder resourcesFolder, ZipOutputStream zipOutStream) {
        DirectoryPersister directoryPersister = ManagerRegistry.getDirectoryPersister();
        for (String filezipEntryName : filesEntries) {
            ZipEntry zipEntry = new ZipEntry(filezipEntryName);
            String resourceName = null;
            try {
                if (!directoryPersister.existFileInRootFolder(filezipEntryName, resourcesFolder.getId(), resourcesFolder.getWorkspace())) {
                    zipOutStream.putNextEntry(zipEntry);
                    if (!filezipEntryName.endsWith("/")) {
                        resourceName = basePath + filezipEntryName;
                        BufferedInputStream inputStream = new BufferedInputStream(ScormUtil.class.getResourceAsStream(resourceName));
                        writeInputStreamIntoOutputStream(inputStream, zipOutStream);
                    }
                }
                zipOutStream.closeEntry();
            } catch (ZipException e) {
                log.warn("ZipException adding control files to exported zip", e);
            } catch (IOException e) {
                String errorAddingFileMessage = "Error adding file to zip" + resourceName;
                log.error(errorAddingFileMessage, e);
                throw new CMSRuntimeException(errorAddingFileMessage, e);
            }
        }
    }
