    public static String hash(String argIn) {
        StringBuffer sb = new StringBuffer();
        String s = null;
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(ourEncryptionMethod);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeEncapsulatingException(e);
        }
        byte[] result = md.digest(argIn.getBytes());
        for (int i = 0; i < result.length; i++) {
            s = Integer.toHexString((int) result[i] & 0xff);
            if (s.length() < 2) sb.append('0');
            sb.append(s);
        }
        return (sb.toString());
    }
