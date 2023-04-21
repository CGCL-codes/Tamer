    public static byte[] md5sum(File dst, MessageDigest digest, byte[] buf) throws IOException {
        digest.reset();
        InputStream in = new DigestInputStream(new FileInputStream(dst), digest);
        try {
            while (in.read(buf) != -1) ;
        } finally {
            in.close();
        }
        return digest.digest();
    }
