    private void calcNetworkKey() {
        byte[] b = accessCfg.getPropertyBytes("network.signature", null);
        MessageDigest md = CryptoUtils.getMessageDigest();
        md.update("secretKey".getBytes());
        networkKey = md.digest(b);
    }
