    public byte[] generateKek(PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        PublicKey vlPublic = this.getValueLinkPublicKey();
        KeyAgreement ka = KeyAgreement.getInstance("DH");
        ka.init(privateKey);
        ka.doPhase(vlPublic, true);
        byte[] secretKey = ka.generateSecret();
        if (debug) {
            Debug.log("Secret Key : " + StringUtil.toHexString(secretKey) + " / " + secretKey.length, module);
        }
        MessageDigest md = MessageDigest.getInstance("SHA1");
        byte[] digest = md.digest(secretKey);
        byte[] des2 = getByteRange(digest, 0, 16);
        byte[] first8 = getByteRange(des2, 0, 8);
        byte[] kek = copyBytes(des2, first8, 0);
        if (debug) {
            Debug.log("Generated KEK : " + StringUtil.toHexString(kek) + " / " + kek.length, module);
        }
        return kek;
    }
