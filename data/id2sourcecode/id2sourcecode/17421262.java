    public static int getHash(InputStream stream) throws IOException {
        InputStream bufferStream = new BufferedInputStream(stream);
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            int totalRead = 0;
            int read = bufferStream.read();
            while (read != -1 && totalRead < 100 * 1000) {
                totalRead++;
                if (totalRead >= 50 * 1000 && totalRead <= 100 * 1000) {
                    digest.update((byte) read);
                }
                read = bufferStream.read();
            }
            return readInteger(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            return -1;
        } finally {
            bufferStream.close();
        }
    }
