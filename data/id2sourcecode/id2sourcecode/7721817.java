    public static String sha1sum(String data) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] digest = md.digest(data.getBytes());
        StringBuffer res = new StringBuffer();
        for (int i = 0; i < digest.length; i++) {
            int digByte = digest[i] & 0xFF;
            if (digByte < 0x10) res.append('0');
            res.append(Integer.toHexString(digByte));
        }
        return res.toString();
    }
