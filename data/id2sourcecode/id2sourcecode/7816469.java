    private ByteBuffer getReadOnlyMemoryMappedBuffer(File file) throws IOException {
        ByteBuffer bb = null;
        FileInputStream in = null;
        FileChannel c = null;
        assert file.exists() : "No file " + file.getAbsolutePath();
        try {
            in = new FileInputStream(file);
            c = in.getChannel();
            int mapSize = (int) Math.min(c.size(), (long) Integer.MAX_VALUE);
            if (mapSize < c.size()) {
                logger.log(Level.WARNING, "only first 2GiB of temp file mapped, thread=" + Thread.currentThread().getName() + " file=" + file);
            }
            bb = c.map(FileChannel.MapMode.READ_ONLY, 0, mapSize).asReadOnlyBuffer();
        } finally {
            if (c != null && c.isOpen()) {
                c.close();
            }
            if (in != null) {
                in.close();
            }
        }
        return bb;
    }
