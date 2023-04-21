    public void putNextEntry(ZipEntry entry) throws IOException {
        if (current != null) closeEntry();
        if (entry.method < 0) entry.method = method;
        if (entry.method == STORED) {
            if (entry.getSize() == -1 || entry.getCrc() == -1) throw new ZipException("required entry not set");
            entry.compressedSize = entry.getSize();
        } else if (outSeekable) entry.compressedSize = entry.size = entry.crc = 0;
        currentStart = bytes_written;
        bytes_written += entry.writeHeader(out, true, bytes_written);
        current = entry;
        int compr = (method == STORED) ? Deflater.NO_COMPRESSION : level;
        def.setLevel(compr);
    }
