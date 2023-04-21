    private void chopKeysBlock(byte[] clientRand, byte[] serverRand, byte[] masterSecret) throws GeneralSecurityException {
        int offset = 0;
        System.arraycopy(keyBlock, 0, clientMACSecret, 0, clientMACSecret.length);
        offset += clientMACSecret.length;
        System.arraycopy(keyBlock, offset, serverMACSecret, 0, serverMACSecret.length);
        offset += serverMACSecret.length;
        System.arraycopy(keyBlock, offset, clientKey, 0, clientKey.length);
        offset += clientKey.length;
        System.arraycopy(keyBlock, offset, serverKey, 0, serverKey.length);
        offset += serverKey.length;
        if (suiteType == Handshake.ARCFOUR_128_MD5 || suiteType == Handshake.ARCFOUR_128_SHA) {
        } else {
            byte[] res = new byte[16];
            md.update(clientKey, 0, clientKey.length);
            md.update(clientRand, 0, clientRand.length);
            md.update(serverRand, 0, serverRand.length);
            md.digest(res, 0, res.length);
            byte[] fcKey = new byte[16];
            System.arraycopy(res, 0, fcKey, 0, fcKey.length);
            md.update(serverKey, 0, serverKey.length);
            md.update(serverRand, 0, serverRand.length);
            md.update(clientRand, 0, clientRand.length);
            md.digest(res, 0, res.length);
            byte[] fserverKey = new byte[fcKey.length];
            System.arraycopy(res, 0, fserverKey, 0, fserverKey.length);
            clientKey = fcKey;
            serverKey = fserverKey;
        }
        clientBulkKey = new SecretKey(clientKey, 0, clientKey.length, "ARC4");
        serverBulkKey = new SecretKey(serverKey, 0, serverKey.length, "ARC4");
    }
