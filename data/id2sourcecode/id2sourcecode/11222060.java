    public boolean storeTransactionToZippedStream(OutputStream outputStream, boolean updateResourceFiles) {
        System.out.println("Writing as a zipped transaction: " + this.uuid);
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        try {
            zipOutputStream.setLevel(Deflater.DEFAULT_COMPRESSION);
            String relativeFileName = ArchiveFileSupport.fileNameInSubdirectoryForTransactionIdentifier(this.uuid);
            ZipEntry zipEntry = new ZipEntry(relativeFileName);
            try {
                zipOutputStream.putNextEntry(zipEntry);
                if (!this.writeTransactionMetadataAndTriplesToUTF8Stream(zipOutputStream)) {
                    return false;
                }
                zipOutputStream.closeEntry();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            for (String transactionResourceFileReference : this.resourceFiles.keySet()) {
                ResourceFileSpecification resourceFileSpecification = this.resourceFiles.get(transactionResourceFileReference);
                zipEntry = new ZipEntry(transactionResourceFileReference);
                zipOutputStream.putNextEntry(zipEntry);
                if (!resourceFileSpecification.copyTo(zipOutputStream, updateResourceFiles)) {
                    return false;
                }
                zipOutputStream.closeEntry();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            this.resetResourceFiles();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            this.resetResourceFiles();
            return false;
        } finally {
            try {
                zipOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
