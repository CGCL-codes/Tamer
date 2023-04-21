    private void writeEntryToStream(ZipOutputStream output, ZipEntry entry, byte[] data) throws IOException {
        entry.setMethod(ZipOutputStream.STORED);
        entry.setSize(data.length);
        entry.setCompressedSize(data.length);
        output.putNextEntry(entry);
        output.write(data);
    }
