    public static String md5sum(String fn) throws IOException {
        RandomAccessFile f = new RandomAccessFile(fn, "r");
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        byte[] buf = new byte[2048];
        while (f.read(buf) != -1) {
            md.update(buf);
        }
        return Util.byteArrayToHexString(md.digest());
    }
