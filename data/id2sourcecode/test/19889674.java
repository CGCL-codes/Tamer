    private static String digest(InputStream input, String algorithm) throws IOException {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            int bufferLength = 1024;
            byte[] buffer = new byte[bufferLength];
            int read = input.read(buffer, 0, bufferLength);
            while (read > -1) {
                messageDigest.update(buffer, 0, read);
                read = input.read(buffer, 0, bufferLength);
            }
            return EncodeUtils.hexEncode(messageDigest.digest());
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Security exception", e);
        }
    }
