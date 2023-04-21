    private static byte[][] getPassword(final Calendar timestamp, final String pinMask, final byte[] salt) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        final long lTimestamp = getTimeStamp(timestamp, 10);
        final String password = pinMask + String.valueOf(lTimestamp);
        final byte[] fullPassword = concatArrays(password.getBytes("8859_1"), salt);
        final MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.reset();
        md5.update(fullPassword);
        final byte[] digest1 = md5.digest();
        md5.reset();
        md5.update(concatArrays(digest1, fullPassword));
        final byte[] digest2 = md5.digest();
        md5.reset();
        md5.update(concatArrays(digest2, fullPassword));
        final byte[] digest3 = md5.digest();
        final byte[][] result = new byte[2][];
        result[0] = concatArrays(digest1, digest2);
        result[1] = digest3;
        return result;
    }
