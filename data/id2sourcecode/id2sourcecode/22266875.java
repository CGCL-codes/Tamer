    private void replaceData() {
        if (worksheetMap.isEmpty() && workbookChanged == false) {
            return;
        }
        byte[] buffer = new byte[BUFFER_SIZE];
        ByteArrayOutputStream out = new ByteArrayOutputStream(2 * fileData.length);
        ZipOutputStream zipOutputStream = new ZipOutputStream(out);
        ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(fileData));
        try {
            zipOutputStream.putNextEntry(new ZipEntry(WORKBOOK_NAME));
            writeDocumentToStream(workbook, zipOutputStream);
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            while (zipEntry != null) {
                if (worksheetMap.containsKey(zipEntry.getName())) {
                    if (log.isDebugEnabled()) {
                        log.debug("Removed old sheet: " + zipEntry.getName());
                    }
                } else if (WORKBOOK_NAME.equals(zipEntry.getName())) {
                    if (log.isDebugEnabled()) {
                        log.debug("Removed old workbook: " + zipEntry.getName());
                    }
                } else {
                    zipOutputStream.putNextEntry(new ZipEntry(zipEntry.getName()));
                    int read = zipInputStream.read(buffer);
                    while (read > -1) {
                        zipOutputStream.write(buffer, 0, read);
                        read = zipInputStream.read(buffer);
                    }
                }
                zipEntry = zipInputStream.getNextEntry();
            }
            for (Map.Entry<String, Document> entry : worksheetMap.entrySet()) {
                zipOutputStream.putNextEntry(new ZipEntry(entry.getKey()));
                writeDocumentToStream(entry.getValue(), zipOutputStream);
            }
        } catch (IOException ex) {
            log.error("Exception while adding or replacing worksheet", ex);
            throw new IllegalArgumentException("fileData is probably not an open xml file: " + ex);
        } finally {
            IOUtils.closeQuietly(zipInputStream);
            IOUtils.closeQuietly(zipOutputStream);
        }
        byte[] result = out.toByteArray();
        if (log.isDebugEnabled()) {
            log.debug("Added or replace worksheet. File data changed from " + fileData.length + " bytes to " + result.length + " bytes.");
        }
        fileData = result;
        worksheetMap.clear();
        workbookChanged = false;
    }
