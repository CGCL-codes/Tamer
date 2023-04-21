    byte[] deriveKey(char id, int len) {
        byte[] key = new byte[len];
        byte[] sharedSecret_K = keyExchanger.getSharedSecret_K();
        byte[] exchangeHash_H = keyExchanger.getExchangeHash_H();
        if (sessionId == null) {
            sessionId = new byte[exchangeHash_H.length];
            System.arraycopy(exchangeHash_H, 0, sessionId, 0, sessionId.length);
        }
        MessageDigest sha1 = keyExchanger.getExchangeHashAlgorithm();
        sha1.update(sharedSecret_K);
        sha1.update(exchangeHash_H);
        sha1.update(new byte[] { (byte) id });
        sha1.update(sessionId);
        byte[] material = sha1.digest();
        int curLen = material.length;
        System.arraycopy(material, 0, key, 0, (curLen < len ? curLen : len));
        while (curLen < len) {
            sha1.reset();
            sha1.update(sharedSecret_K);
            sha1.update(exchangeHash_H);
            sha1.update(key, 0, curLen);
            material = sha1.digest();
            if (len - curLen > material.length) System.arraycopy(material, 0, key, curLen, material.length); else System.arraycopy(material, 0, key, curLen, len - curLen);
            curLen += material.length;
        }
        tpLog.debug2("SSH2Transport", "deriveKey", "key id " + id, key);
        return key;
    }
