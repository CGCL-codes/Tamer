    private char[] md5Digest(File file) throws NoSuchAlgorithmException, IOException {
        DigestInputStream dis = null;
        try {
            dis = new DigestInputStream(new FileInputStream(file), MessageDigest.getInstance("MD5"));
            byte[] buf = new byte[32];
            while (dis.read(buf) > 0) {
            }
            return MD5Utils.toHexChars(dis.getMessageDigest().digest());
        } finally {
            if (dis != null) dis.close();
        }
    }
