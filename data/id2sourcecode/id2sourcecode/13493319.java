    public void putNextEntry(ZipEntryEx e) throws IOException {
        ensureOpen();
        if (entry != null) {
            closeEntry();
        }
        if (e.getTime() == -1) {
            e.setTime(System.currentTimeMillis());
        }
        if (e.getMethod() == -1) {
            e.setMethod(method);
        }
        switch(e.getMethod()) {
            case DEFLATED:
                if (e.getSize() == -1 || e.getCompressedSize() == -1 || e.getCrc() == -1) {
                    e.flag = 8;
                } else if (e.getSize() != -1 && e.getCompressedSize() != -1 && e.getCrc() != -1) {
                    e.flag = 0;
                } else {
                    throw new ZipException("DEFLATED entry missing size, compressed size, or crc-32");
                }
                e.version = 20;
                break;
            case STORED:
                if (e.getSize() == -1) {
                    e.setSize(e.getCompressedSize());
                } else if (e.getCompressedSize() == -1) {
                    e.setCompressedSize(e.getSize());
                } else if (e.getSize() != e.getCompressedSize()) {
                    throw new ZipException("STORED entry where compressed != uncompressed size");
                }
                if (e.getSize() == -1 || e.getCrc() == -1) {
                    throw new ZipException("STORED entry missing size, compressed size, or crc-32");
                }
                e.version = 10;
                e.flag = 0;
                break;
            default:
                throw new ZipException("unsupported compression method");
        }
        e.offset = written;
        if (names.put(e.getName(), e) != null) {
            throw new ZipException("duplicate entry: " + e.getName());
        }
        writeLOC(e);
        entries.addElement(e);
        entry = e;
    }
