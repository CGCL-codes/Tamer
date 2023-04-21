    protected static byte[] getHeadMD5(File file, long chunkSize) throws IOException {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            FileChannel channel = new FileInputStream(file).getChannel();
            try {
                md5.update(channel.map(MapMode.READ_ONLY, 0, min(channel.size(), chunkSize)));
            } finally {
                channel.close();
            }
            return md5.digest();
        } catch (Exception e) {
            throw new IOException("Failed to calculate md5 hash", e);
        }
    }
