    public void putNextEntry(ZipEntry e) throws IOException {
        ensureOpen();
        if (entry != null) {
            closeEntry();
        }
        if (e.time == -1) {
            e.setTime(System.currentTimeMillis());
        }
        if (e.method == -1) {
            e.method = method;
        }
        switch(e.method) {
            case DEFLATED:
                if (e.size == -1 || e.csize == -1 || e.crc == -1) {
                    e.flag = 8;
                } else if (e.size != -1 && e.csize != -1 && e.crc != -1) {
                    e.flag = 0;
                } else {
                    throw new ZipException("DEFLATED entry missing size, compressed size, or crc-32");
                }
                e.version = 20;
                break;
            case STORED:
                if (e.size == -1) {
                    e.size = e.csize;
                } else if (e.csize == -1) {
                    e.csize = e.size;
                } else if (e.size != e.csize) {
                    throw new ZipException("STORED entry where compressed != uncompressed size");
                }
                if (e.size == -1 || e.crc == -1) {
                    throw new ZipException("STORED entry missing size, compressed size, or crc-32");
                }
                e.version = 10;
                e.flag = 0;
                break;
            default:
                throw new ZipException("unsupported compression method");
        }
        e.offset = written;
        if (names.put(e.name, e) != null) {
            throw new ZipException("duplicate entry: " + e.name);
        }
        writeLOC(e);
        entries.addElement(e);
        entry = e;
    }
