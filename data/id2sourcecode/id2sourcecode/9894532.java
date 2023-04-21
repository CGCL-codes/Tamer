    private void modifyFileHeader(final InputStream source, final OutputStream destination, final long fileSizeDiff) throws IOException {
        destination.write(GUID.GUID_FILE.getBytes());
        final long chunkSize = Utils.readUINT64(source);
        Utils.writeUINT64(chunkSize, destination);
        destination.write(Utils.readGUID(source).getBytes());
        final long fileSize = Utils.readUINT64(source);
        Utils.writeUINT64(fileSize + fileSizeDiff, destination);
        Utils.copy(source, destination, chunkSize - 48);
    }
