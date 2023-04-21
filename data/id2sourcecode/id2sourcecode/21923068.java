    public static String digest(String filename, String algorithm) throws Exception {
        FileInputStream fis = new FileInputStream(filename);
        MessageDigest md = MessageDigest.getInstance(algorithm);
        try {
            DigestInputStream dis = new DigestInputStream(fis, md);
            byte[] buffer = new byte[8192];
            while (dis.read(buffer) != -1) ;
        } finally {
            fis.close();
        }
        String result = byteArrayToHexString(md.digest());
        return result;
    }
