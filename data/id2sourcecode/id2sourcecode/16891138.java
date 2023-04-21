    public static byte[] writeKeyPairSSHCom(String password, String cipher, KeyPair keyPair) throws SSH2FatalException {
        SSH2DataBuffer toBeEncrypted = new SSH2DataBuffer(8192);
        int totLen = 0;
        DSAPublicKey pubKey = (DSAPublicKey) keyPair.getPublic();
        DSAPrivateKey prvKey = (DSAPrivateKey) keyPair.getPrivate();
        DSAParams params = pubKey.getParams();
        if (!(pubKey instanceof DSAPublicKey)) {
            throw new SSH2FatalException("Unsupported key type: " + pubKey);
        }
        toBeEncrypted.writeInt(0);
        toBeEncrypted.writeInt(0);
        toBeEncrypted.writeBigIntBits(params.getP());
        toBeEncrypted.writeBigIntBits(params.getG());
        toBeEncrypted.writeBigIntBits(params.getQ());
        toBeEncrypted.writeBigIntBits(pubKey.getY());
        toBeEncrypted.writeBigIntBits(prvKey.getX());
        totLen = toBeEncrypted.getWPos();
        toBeEncrypted.setWPos(0);
        toBeEncrypted.writeInt(totLen - 4);
        if (!cipher.equals("none")) {
            try {
                int keyLen = SSH2Preferences.getCipherKeyLen(cipher);
                String cipherName = SSH2Preferences.ssh2ToJCECipher(cipher);
                byte[] key = expandPasswordToKeySSHCom(password, keyLen);
                Cipher encrypt = Cipher.getInstance(cipherName);
                encrypt.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, encrypt.getAlgorithm()));
                byte[] data = toBeEncrypted.getData();
                int bs = encrypt.getBlockSize();
                totLen += (bs - (totLen % bs)) % bs;
                totLen = encrypt.doFinal(data, 0, totLen, data, 0);
            } catch (NoSuchAlgorithmException e) {
                throw new SSH2FatalException("Invalid cipher in " + "SSH2KeyPairFile.writeKeyPair: " + cipher);
            } catch (InvalidKeyException e) {
                throw new SSH2FatalException("Invalid key derived in " + "SSH2KeyPairFile.writeKeyPair: " + e);
            }
        }
        SSH2DataBuffer buf = new SSH2DataBuffer(512 + totLen);
        buf.writeInt(SSH_PRIVATE_KEY_MAGIC);
        buf.writeInt(0);
        buf.writeString("dl-modp{sign{dsa-nist-sha1},dh{plain}}");
        buf.writeString(cipher);
        buf.writeString(toBeEncrypted.getData(), 0, totLen);
        totLen = buf.getWPos();
        buf.setWPos(4);
        buf.writeInt(totLen);
        byte[] keyBlob = new byte[totLen];
        System.arraycopy(buf.data, 0, keyBlob, 0, totLen);
        return keyBlob;
    }
