    private String createDirectoryName(Integer id) throws NoSuchAlgorithmException {
        StringBuffer uniqueDirectoryName = new StringBuffer();
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.reset();
        md5.update(id.byteValue());
        byte digest[] = md5.digest();
        for (int i = 0; i < digest.length; i++) {
            uniqueDirectoryName.append(Integer.toHexString(0xFF & digest[i]));
        }
        return uniqueDirectoryName.toString();
    }
