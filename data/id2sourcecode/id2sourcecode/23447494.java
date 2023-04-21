    private static String md5Representation(String data) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] digest = messageDigest.digest(data.getBytes("US-ASCII"));
            return byteArrayToString(digest);
        } catch (NoSuchAlgorithmException nsae) {
            throw new RuntimeException("No MD5 algorithm");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("No UTF-8");
        }
    }
