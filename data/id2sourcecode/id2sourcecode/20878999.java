    protected void writeZip(Object o, OutputStream out, String entryName) {
        try {
            ZipOutputStream zipStream = new ZipOutputStream(out);
            zipStream.setLevel(compressLevel);
            ZipEntry entry = new ZipEntry(entryName);
            zipStream.putNextEntry(entry);
            writeStream(o, zipStream);
            zipStream.closeEntry();
            zipStream.finish();
            zipStream.close();
            closeWriteStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
