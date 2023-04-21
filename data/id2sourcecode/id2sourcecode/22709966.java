    protected void writeMedia(InputStream is, int byte_count, ServletOutputStream out) throws IOException {
        if (is == null || byte_count < 0 || out == null) throw new IllegalArgumentException();
        if (log.isDebugEnabled()) log.debug("writeMedia: count=" + byte_count);
        int block_size = (byte_count > MAX_BLOCK_SIZE ? MAX_BLOCK_SIZE : byte_count);
        byte[] buf = new byte[block_size];
        int remaining = byte_count;
        ExpiringWriter writer = new ExpiringWriter(out, WRITE_TIMEOUT);
        while (remaining > 0) {
            int bytes_to_read = (remaining < block_size ? remaining : block_size);
            if (bytes_to_read < 1) break;
            int bytes_read = is.read(buf, 0, bytes_to_read);
            if (bytes_read < 1) break;
            if (log.isDebugEnabled()) log.debug("writeMedia: writing " + bytes_read + " byte(s)");
            if (!writer.write(buf, 0, bytes_read)) break;
            remaining -= bytes_read;
        }
    }
