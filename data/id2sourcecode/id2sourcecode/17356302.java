    public static byte[] writeRandomFile(OutputStream stream, int fileLength, Random randBytes) throws IOException {
        DigestOutputStream digestStreamWrapper = null;
        try {
            digestStreamWrapper = new DigestOutputStream(stream, MessageDigest.getInstance("SHA1"));
        } catch (NoSuchAlgorithmException e) {
            Log.severe("No SHA1 available!");
            Assert.fail("No SHA1 available!");
        }
        byte[] bytes = new byte[BUF_SIZE];
        int elapsed = 0;
        int nextBufSize = 0;
        final double probFlush = .3;
        while (elapsed < fileLength) {
            nextBufSize = ((fileLength - elapsed) > BUF_SIZE) ? BUF_SIZE : (fileLength - elapsed);
            randBytes.nextBytes(bytes);
            digestStreamWrapper.write(bytes, 0, nextBufSize);
            elapsed += nextBufSize;
            if (randBytes.nextDouble() < probFlush) {
                System.out.println("Flushing buffers, have written " + elapsed + " bytes out of " + fileLength);
                digestStreamWrapper.flush();
            }
        }
        digestStreamWrapper.close();
        return digestStreamWrapper.getMessageDigest().digest();
    }
