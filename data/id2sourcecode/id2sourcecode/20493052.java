    public static boolean copyToFile(InputStream from, File to) {
        try {
            FileChannel toChannel = new FileOutputStream(to).getChannel();
            byte[] buf = new byte[1024 * 8];
            int howMany = 0;
            while ((howMany = from.read(buf, 0, buf.length)) >= 0) {
                toChannel.write(ByteBuffer.wrap(buf, 0, howMany));
            }
            toChannel.close();
        } catch (IOException e) {
            log.error("failed to copy input stream to " + to.getAbsolutePath() + ": caught exception", e);
            return false;
        }
        return true;
    }
