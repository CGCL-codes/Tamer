    public static String getNapiMd5sum(File file) throws NoSuchAlgorithmException, IOException {
        int length;
        if (file.length() > 10485760) {
            length = 10485760;
        } else {
            length = (int) file.length();
        }
        byte[] buffer = new byte[length];
        FileInputStream fis = new FileInputStream(file);
        fis.read(buffer, 0, length);
        fis.close();
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(buffer);
        byte[] hash = md5.digest();
        StringBuffer hexString = new StringBuffer();
        String hexPart;
        for (int i = 0; i < hash.length; i++) {
            hexPart = Integer.toHexString(0xFF & hash[i]);
            if (hexPart.length() == 1) {
                hexPart = "0" + hexPart;
            }
            hexString.append(hexPart);
        }
        return hexString.toString();
    }
