    public static PGPPublicKeyRing importRemoteKey(String iUrl) throws Exception {
        URL url = new URL(iUrl);
        InputStream is = url.openStream();
        PGPPublicKeyRing outKey = KeyRing.importPublicKey(is);
        is.close();
        return outKey;
    }
