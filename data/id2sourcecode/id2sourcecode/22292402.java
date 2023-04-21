    public static String calculateSHA1Hash(String source) throws NoSuchAlgorithmException {
        MessageDigest md;
        byte[] buffer, digest;
        String hash = "";
        buffer = source.getBytes();
        md = MessageDigest.getInstance("SHA1");
        md.update(buffer);
        digest = md.digest();
        for (byte aux : digest) {
            int b = aux & 0xff;
            if (Integer.toHexString(b).length() == 1) hash += "0";
            hash += Integer.toHexString(b);
        }
        return hash;
    }
