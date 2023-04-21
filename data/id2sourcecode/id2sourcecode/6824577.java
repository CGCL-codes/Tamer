    public void writeStoredEntry(String name, byte[] data) throws IOException {
        ZipEntry ze = new ZipEntry(name);
        ze.setMethod(ZipEntry.STORED);
        ze.setCompressedSize(data.length);
        ze.setSize(data.length);
        crc.reset();
        crc.update(data);
        ze.setCrc(crc.getValue());
        putNextEntry(ze);
        write(data);
        closeEntry();
    }
