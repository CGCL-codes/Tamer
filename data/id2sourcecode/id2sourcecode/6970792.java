    void addDirectoryFileToZip(DirectoryFile file, ZipOutputStream zipOutStream, String previousPath, String filePathtoConverTags, ExportContentData exportContentData) {
        log.debug("File " + file.getName() + " previousPath: " + previousPath);
        try {
            BufferedInputStream in = new BufferedInputStream(getInputStreamFromPath(file));
            ZipEntry zipEntry = new ZipEntry(previousPath + file.getName());
            try {
                try {
                    zipOutStream.putNextEntry(zipEntry);
                } catch (ZipException e) {
                    log.warn("File " + file.getName() + " is already at the zip. Ignored");
                    zipOutStream.closeEntry();
                    return;
                }
                if (filePathtoConverTags != null) {
                    String chapterHTMLContent = writeInputStreamIntoString(in);
                    String contentResourcesFolderId = filePathtoConverTags.substring(0, 36);
                    String relativeChapterPath = filePathtoConverTags.substring(37, filePathtoConverTags.length());
                    String convertedChapterContent = convertToLocalUrls(chapterHTMLContent, zipOutStream, contentResourcesFolderId, relativeChapterPath, file.getWorkspace(), exportContentData);
                    in = new BufferedInputStream(new ByteArrayInputStream(convertedChapterContent.getBytes()));
                }
                writeInputStreamIntoOutputStream(in, zipOutStream);
                zipOutStream.closeEntry();
            } catch (IOException e) {
                String errorMessage = "Error adding file " + previousPath + file.getName();
                log.error(errorMessage, e);
                throw new CMSRuntimeException(errorMessage, e);
            }
        } catch (MalformedDirectoryItemException e) {
            log.error("Cannot add file to zip. Item " + file.getId() + " is malformed");
        }
    }
