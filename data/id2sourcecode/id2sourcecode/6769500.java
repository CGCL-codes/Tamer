    private SecretKey engineGenerateKey0() throws GeneralSecurityException {
        byte[] masterSecret = spec.getMasterSecret().getEncoded();
        byte[] clientRandom = spec.getClientRandom();
        byte[] serverRandom = spec.getServerRandom();
        SecretKey clientMacKey = null;
        SecretKey serverMacKey = null;
        SecretKey clientCipherKey = null;
        IvParameterSpec clientIv = null;
        SecretKey serverCipherKey = null;
        IvParameterSpec serverIv = null;
        int macLength = spec.getMacKeyLength();
        int expandedKeyLength = spec.getExpandedCipherKeyLength();
        boolean isExportable = (expandedKeyLength != 0);
        int keyLength = spec.getCipherKeyLength();
        int ivLength = spec.getIvLength();
        int keyBlockLen = macLength + keyLength + (isExportable ? 0 : ivLength);
        keyBlockLen <<= 1;
        byte[] keyBlock = new byte[keyBlockLen];
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        MessageDigest sha = MessageDigest.getInstance("SHA1");
        if (protocolVersion >= 0x0301) {
            byte[] seed = concat(serverRandom, clientRandom);
            keyBlock = doPRF(masterSecret, LABEL_KEY_EXPANSION, seed, keyBlockLen, md5, sha);
        } else {
            keyBlock = new byte[keyBlockLen];
            byte[] tmp = new byte[20];
            for (int i = 0, remaining = keyBlockLen; remaining > 0; i++, remaining -= 16) {
                sha.update(SSL3_CONST[i]);
                sha.update(masterSecret);
                sha.update(serverRandom);
                sha.update(clientRandom);
                sha.digest(tmp, 0, 20);
                md5.update(masterSecret);
                md5.update(tmp);
                if (remaining >= 16) {
                    md5.digest(keyBlock, i << 4, 16);
                } else {
                    md5.digest(tmp, 0, 16);
                    System.arraycopy(tmp, 0, keyBlock, i << 4, remaining);
                }
            }
        }
        int ofs = 0;
        byte[] tmp = new byte[macLength];
        System.arraycopy(keyBlock, ofs, tmp, 0, macLength);
        ofs += macLength;
        clientMacKey = new SecretKeySpec(tmp, "Mac");
        System.arraycopy(keyBlock, ofs, tmp, 0, macLength);
        ofs += macLength;
        serverMacKey = new SecretKeySpec(tmp, "Mac");
        if (keyLength == 0) {
            return new TlsKeyMaterialSpec(clientMacKey, serverMacKey);
        }
        String alg = spec.getCipherAlgorithm();
        byte[] clientKeyBytes = new byte[keyLength];
        System.arraycopy(keyBlock, ofs, clientKeyBytes, 0, keyLength);
        ofs += keyLength;
        byte[] serverKeyBytes = new byte[keyLength];
        System.arraycopy(keyBlock, ofs, serverKeyBytes, 0, keyLength);
        ofs += keyLength;
        if (isExportable == false) {
            clientCipherKey = new SecretKeySpec(clientKeyBytes, alg);
            serverCipherKey = new SecretKeySpec(serverKeyBytes, alg);
            if (ivLength != 0) {
                tmp = new byte[ivLength];
                System.arraycopy(keyBlock, ofs, tmp, 0, ivLength);
                ofs += ivLength;
                clientIv = new IvParameterSpec(tmp);
                System.arraycopy(keyBlock, ofs, tmp, 0, ivLength);
                ofs += ivLength;
                serverIv = new IvParameterSpec(tmp);
            }
        } else {
            if (protocolVersion >= 0x0301) {
                byte[] seed = concat(clientRandom, serverRandom);
                tmp = doPRF(clientKeyBytes, LABEL_CLIENT_WRITE_KEY, seed, expandedKeyLength, md5, sha);
                clientCipherKey = new SecretKeySpec(tmp, alg);
                tmp = doPRF(serverKeyBytes, LABEL_SERVER_WRITE_KEY, seed, expandedKeyLength, md5, sha);
                serverCipherKey = new SecretKeySpec(tmp, alg);
                if (ivLength != 0) {
                    tmp = new byte[ivLength];
                    byte[] block = doPRF(null, LABEL_IV_BLOCK, seed, ivLength << 1, md5, sha);
                    System.arraycopy(block, 0, tmp, 0, ivLength);
                    clientIv = new IvParameterSpec(tmp);
                    System.arraycopy(block, ivLength, tmp, 0, ivLength);
                    serverIv = new IvParameterSpec(tmp);
                }
            } else {
                tmp = new byte[expandedKeyLength];
                md5.update(clientKeyBytes);
                md5.update(clientRandom);
                md5.update(serverRandom);
                System.arraycopy(md5.digest(), 0, tmp, 0, expandedKeyLength);
                clientCipherKey = new SecretKeySpec(tmp, alg);
                md5.update(serverKeyBytes);
                md5.update(serverRandom);
                md5.update(clientRandom);
                System.arraycopy(md5.digest(), 0, tmp, 0, expandedKeyLength);
                serverCipherKey = new SecretKeySpec(tmp, alg);
                if (ivLength != 0) {
                    tmp = new byte[ivLength];
                    md5.update(clientRandom);
                    md5.update(serverRandom);
                    System.arraycopy(md5.digest(), 0, tmp, 0, ivLength);
                    clientIv = new IvParameterSpec(tmp);
                    md5.update(serverRandom);
                    md5.update(clientRandom);
                    System.arraycopy(md5.digest(), 0, tmp, 0, ivLength);
                    serverIv = new IvParameterSpec(tmp);
                }
            }
        }
        return new TlsKeyMaterialSpec(clientMacKey, serverMacKey, clientCipherKey, clientIv, serverCipherKey, serverIv);
    }
