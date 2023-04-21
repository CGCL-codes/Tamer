    public static byte[] encoderForBytes(String source) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            char[] charArray = source.toCharArray();
            byte[] byteArray = new byte[charArray.length];
            for (int i = 0; i < charArray.length; i++) byteArray[i] = (byte) charArray[i];
            return digest.digest(byteArray);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
