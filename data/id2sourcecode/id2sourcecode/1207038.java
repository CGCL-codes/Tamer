    public static String getMD5String(byte[] bytes) {
        messageDigest.update(bytes);
        return bytesToHex(messageDigest.digest());
    }
