    public synchronized KeyPair doCA(int keyId, PublicKey key) throws CardServiceException {
        try {
            String algName = (key instanceof ECPublicKey) ? "ECDH" : "DH";
            KeyPairGenerator genKey = KeyPairGenerator.getInstance(algName);
            AlgorithmParameterSpec spec = null;
            if ("DH".equals(algName)) {
                DHPublicKey k = (DHPublicKey) key;
                spec = k.getParams();
            } else {
                ECPublicKey k = (ECPublicKey) key;
                spec = k.getParams();
            }
            genKey.initialize(spec);
            KeyPair keyPair = genKey.generateKeyPair();
            KeyAgreement agreement = KeyAgreement.getInstance("ECDH", "BC");
            agreement.init(keyPair.getPrivate());
            agreement.doPhase(key, true);
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] secret = md.digest(agreement.generateSecret());
            byte[] keyData = null;
            if ("DH".equals(algName)) {
                DHPublicKey k = (DHPublicKey) keyPair.getPublic();
                keyData = k.getY().toByteArray();
            } else {
                org.bouncycastle.jce.interfaces.ECPublicKey k = (org.bouncycastle.jce.interfaces.ECPublicKey) keyPair.getPublic();
                keyData = k.getQ().getEncoded();
            }
            keyData = tagData((byte) 0x91, keyData);
            sendMSE(wrapper, 0x41, 0xA6, keyData);
            SecretKey ksEnc = Util.deriveKey(secret, Util.ENC_MODE);
            SecretKey ksMac = Util.deriveKey(secret, Util.MAC_MODE);
            wrapper = new SecureMessagingWrapper(ksEnc, ksMac, 0L);
            state = CA_AUTHENTICATED_STATE;
            return keyPair;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new CardServiceException("Problem occured during Chip Authentication: " + ex.getMessage());
        }
    }
