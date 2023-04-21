    public byte[] getSignature(PrivateKey privateKey) throws Exception {
        if (privateKey == null) {
            throw new IllegalArgumentException("null private key");
        }
        final MessageDigest sha256 = MessageDigest.getInstance("SHA256", new BouncyCastleProvider());
        byte[] signedInfoDigest = sha256.digest(getXML().getBytes());
        byte[] digestToSign = new byte[RSA_SHA256prefix.length + signedInfoDigest.length];
        System.arraycopy(RSA_SHA256prefix, 0, digestToSign, 0, RSA_SHA256prefix.length);
        System.arraycopy(signedInfoDigest, 0, digestToSign, RSA_SHA256prefix.length, signedInfoDigest.length);
        System.out.println("SignedInfo SHA-256 HASH: " + new String(Hex.encode(digestToSign)));
        Signature sig = Signature.getInstance("RSA", "BC");
        sig.initSign(privateKey);
        sig.update(digestToSign);
        byte[] result = sig.sign();
        return result;
    }
