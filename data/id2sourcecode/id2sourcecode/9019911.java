    private static String createServerKey() {
        byte[] tmpKey;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA1");
            tmpKey = digest.digest(("" + new Date()).getBytes());
        } catch (NoSuchAlgorithmException e1) {
            tmpKey = new byte[3];
            new Random().nextBytes(tmpKey);
        }
        StringBuffer keyBuffer = new StringBuffer();
        for (int i = 0; i < 3; i++) {
            keyBuffer.append(Integer.toHexString(tmpKey[i] & 0xff).toLowerCase());
        }
        return keyBuffer.toString();
    }
