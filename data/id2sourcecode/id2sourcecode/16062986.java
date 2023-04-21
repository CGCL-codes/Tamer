    public static String md5sumFile(String filename) {
        DigestInputStream dis = null;
        try {
            dis = new DigestInputStream(new FileInputStream(filename), MessageDigest.getInstance("MD5"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        try {
            while (dis.read() > 0) {
                continue;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return byte2HexString(dis.getMessageDigest().digest());
    }
