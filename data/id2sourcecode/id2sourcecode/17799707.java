    private BencodeString calcDHTKey() {
        byte[] b = accessCfg.getPropertyBytes("network.signature", null);
        MessageDigest md = CryptoUtils.getMessageDigest();
        md.update("secretKeyDHT".getBytes());
        return new BencodeString(md.digest(b));
    }
