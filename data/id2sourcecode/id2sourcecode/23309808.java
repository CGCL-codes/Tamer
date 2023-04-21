    public OutputStream getOutputStream(String name, boolean eligibleForCompression) throws IOException {
        if (eligibleForCompression) {
            zip.putNextEntry(new ZipEntry(name));
            return new CompressedEntryStream();
        } else {
            return new StoredEntryStream(name);
        }
    }
