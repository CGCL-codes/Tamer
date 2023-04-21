    public static String getMD5Hash(byte[] bytes) throws NoSuchAlgorithmException {
        byte[] key = "PATIENTISAUTHENTICATION".getBytes();
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(bytes);
        return new String(HashUtility.base64Encode(md5.digest(key)));
    }
