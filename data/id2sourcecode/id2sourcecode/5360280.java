    public void putNextEntry(ZipEntry ze) throws IOException {
        closeEntry();
        entry = ze;
        entries.addElement(entry);
        if (entry.getMethod() == -1) {
            entry.setMethod(method);
        }
        if (entry.getTime() == -1) {
            entry.setTime(System.currentTimeMillis());
        }
        if (entry.getMethod() == STORED && raf == null) {
            if (entry.getSize() == -1) {
                throw new ZipException("uncompressed size is required for" + " STORED method when not writing to a" + " file");
            }
            if (entry.getCrc() == -1) {
                throw new ZipException("crc checksum is required for STORED" + " method when not writing to a file");
            }
            entry.setCompressedSize(entry.getSize());
        }
        if (entry.getMethod() == DEFLATED && hasCompressionLevelChanged) {
            def.setLevel(level);
            hasCompressionLevelChanged = false;
        }
        writeLocalFileHeader(entry);
    }
