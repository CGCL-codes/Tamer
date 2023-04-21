    private static void example() throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        InputStream is = new FileInputStream("file.txt");
        try {
            is = new DigestInputStream(is, md);
        } finally {
            is.close();
        }
        byte[] digest = md.digest();
        StringBuilder buffer = new StringBuilder(digest.length * 2);
        for (byte b : digest) {
            buffer.append(Integer.toString(b, 16));
        }
        String md5 = buffer.toString();
    }
