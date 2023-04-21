    public boolean validateKeyHash(String keyHashString) {
        try {
            RSAKey rsa = (RSAKey) publicKey;
            BigInteger modulus = rsa.getModulus();
            byte[] modbytes = modulus.toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(modbytes);
            byte[] keyHash = md.digest();
            if (keyHashString.equals(Base64.encodeBytes(keyHash))) return true;
        } catch (Exception e) {
            logger.severe("Error validating keyhash");
            logger.severe(e.toString());
            logger.severe(Util.getStackTop(e));
        }
        return false;
    }
