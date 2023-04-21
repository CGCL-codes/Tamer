    public static void md5sum(File fileOrDir, char[] cbuf, MessageDigest digest, byte[] bbuf) throws IOException {
        digest.reset();
        InputStream in = new DigestInputStream(new FileInputStream(fileOrDir), digest);
        try {
            while (in.read(bbuf) != -1) ;
        } finally {
            in.close();
        }
        toHexChars(digest.digest(), cbuf);
    }
