    private static String calcCommonName(PublicKey pubKey) {
        byte[] data = pubKey.getEncoded();
        byte[] digest = null;
        try {
            MessageDigest msgDgst = MessageDigest.getInstance("SHA-1");
            msgDgst.update(data);
            digest = msgDgst.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        int subHash = 0;
        subHash = digest[0] + (digest[1] + (digest[2] + (digest[3] >>> 1) * 256) * 256) * 256;
        return Integer.toString(Math.abs(subHash));
    }
