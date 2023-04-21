    public static String getHash(String message) throws NoSuchAlgorithmException {
        byte[] buffer = message.getBytes();
        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.update(buffer);
        byte[] digest = md.digest();
        String hash = "";
        for (byte aux : digest) {
            int b = aux & 0xff;
            if (Integer.toHexString(b).length() == 1) hash += "0";
            hash += Integer.toHexString(b);
        }
        return hash;
    }
