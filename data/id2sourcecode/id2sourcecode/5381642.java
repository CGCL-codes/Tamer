    public static String digest(String stringa) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
        byte[] digest = messageDigest.digest(stringa.getBytes());
        StringBuffer hash = new StringBuffer();
        String tmp;
        for (byte b : digest) {
            tmp = Integer.toHexString(b + 128);
            hash.append(tmp.length() == 1 ? "0" + tmp : tmp);
        }
        return hash.toString();
    }
