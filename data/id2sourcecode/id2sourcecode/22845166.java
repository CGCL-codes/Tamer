    private static String generateMessageHash(DebugInformation debugInfo) {
        String hash = "";
        byte[] bytes = debugInfo.toString().getBytes();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(bytes);
            byte messageDigest[] = algorithm.digest();
            StringBuffer hexString = new StringBuffer();
            for (byte messageByte : messageDigest) {
                hexString.append(Integer.toHexString(0xFF & messageByte));
            }
            hash = hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash;
    }
