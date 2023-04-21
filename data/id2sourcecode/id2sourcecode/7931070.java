    public static String checksum(InputStream in, String algorithm) throws IOException {
        try {
            DigestInputStream din = new DigestInputStream(in, MessageDigest.getInstance(algorithm));
            while (true) {
                synchronized (buffer) {
                    if (din.read(buffer) == -1) {
                        break;
                    }
                }
            }
            return toHex(din.getMessageDigest().digest());
        } catch (NoSuchAlgorithmException nsaE) {
            throw new IOException(nsaE.getMessage(), nsaE);
        }
    }
