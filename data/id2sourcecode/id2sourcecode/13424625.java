    private byte[] networkHash(int maxLen) {
        byte[] b = connectionManager.getAccessCfg().getPropertyBytes("network.signature", null);
        MessageDigest md = CryptoUtils.getMessageDigest();
        md.update("BitTorrent".getBytes());
        byte[] hash = md.digest(b);
        byte[] result = new byte[Math.min(maxLen, hash.length)];
        System.arraycopy(hash, 0, result, 0, result.length);
        return result;
    }
