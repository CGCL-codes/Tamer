    public void writeDocument(DocumentData document) throws IndexException {
        if (zipFile == null) openZipFile();
        try {
            ObjectOutputStream objectOutStream = new ObjectOutputStream(byteArrayOS);
            objectOutStream.writeObject(document);
            objectOutStream.close();
            if (currentEntries >= ZIP_FILE_MAX_ENTRIES || currentLength + byteArrayOS.size() >= ZIP_FILE_MAX_SIZE) {
                closeZipFile();
                zipFileId++;
                openZipFile();
            }
            ZipEntry entry = new ZipEntry(Integer.toString(documentId++));
            zipOuputStream.putNextEntry(entry);
            byteArrayOS.writeTo(zipOuputStream);
            zipOuputStream.closeEntry();
            currentLength += entry.getCompressedSize();
            byteArrayOS.reset();
            currentEntries++;
        } catch (IOException e) {
            throw new IndexException("Problem while accessing the collection file", e);
        }
    }
