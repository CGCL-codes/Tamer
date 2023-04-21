    public void setPublicKey(PublicKey pub) {
        this.publicKey = pub;
        try {
            RSAKey rsa = (RSAKey) pub;
            BigInteger modulus = rsa.getModulus();
            byte[] modbytes = modulus.toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(modbytes);
            keyHash = md.digest();
            logger.info("set public key");
        } catch (Exception e) {
            logger.severe("Error setting MintInfo public key");
            logger.severe(e.toString());
        }
    }
