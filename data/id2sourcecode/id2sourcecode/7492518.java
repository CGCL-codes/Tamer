    private static void addScormDocumentControlFiles(ZipOutputStream zipOutStream) {
        List<String> scormControlFilesZipEntriesNames = CmsConfig.getScormControlFilesZipEntries();
        for (String scormControlFilezipEntryName : scormControlFilesZipEntriesNames) {
            ZipEntry zipEntry = new ZipEntry(scormControlFilezipEntryName);
            String resourceName = null;
            try {
                zipOutStream.putNextEntry(zipEntry);
                if (!scormControlFilezipEntryName.endsWith("/")) {
                    resourceName = CmsConfig.getScormControlFilesDirectoryPath() + scormControlFilezipEntryName;
                    BufferedInputStream inputStream = new BufferedInputStream(SCORMUtil.class.getResourceAsStream(resourceName));
                    writeInputStreamIntoOutputStream(inputStream, zipOutStream);
                }
                zipOutStream.closeEntry();
            } catch (ZipException e) {
                String warningMessage = "ZipException adding control files to exported SCORM zip";
                log.warn(warningMessage, e);
            } catch (IOException e) {
                String errorMessage = "Error adding scorm control file " + resourceName;
                log.error(errorMessage, e);
                throw new CMSRuntimeException(errorMessage, e);
            }
        }
    }
