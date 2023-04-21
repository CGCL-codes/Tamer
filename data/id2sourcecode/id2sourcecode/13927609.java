    public static List<byte[]> createTreeNodes(int nodeSize, long fileSize, InputStream is, MessageDigest messageDigest) throws IOException {
        List<byte[]> ret = new ArrayList<byte[]>((int) Math.ceil((double) fileSize / nodeSize));
        MessageDigest tt = new MerkleTree(messageDigest);
        byte[] block = new byte[HashTreeUtils.BLOCK_SIZE * 128];
        long offset = 0;
        int read = 0;
        while (offset < fileSize) {
            int nodeOffset = 0;
            long time = System.currentTimeMillis();
            tt.reset();
            while (nodeOffset < nodeSize && (read = is.read(block)) != -1) {
                tt.update(block, 0, read);
                nodeOffset += read;
                offset += read;
                try {
                    long sleep = (System.currentTimeMillis() - time) * 2;
                    if (sleep > 0) Thread.sleep(sleep);
                } catch (InterruptedException ie) {
                    throw new IOException("interrupted during hashing operation");
                }
                time = System.currentTimeMillis();
            }
            ret.add(tt.digest());
            if (offset == fileSize) {
                if (read != -1 && is.read() != -1) {
                    LOG.warn("More data than fileSize!");
                    throw new IOException("unknown file size.");
                }
            } else if (read == -1 && offset != fileSize) {
                if (LOG.isWarnEnabled()) {
                    LOG.warn("couldn't hash whole file. " + "read: " + read + ", offset: " + offset + ", fileSize: " + fileSize);
                }
                throw new IOException("couldn't hash whole file.");
            }
        }
        return ret;
    }
