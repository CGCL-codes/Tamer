    public byte[] expectedReturnValue() {
        try {
            byte[] sigBytes = icu.getSignatureBytes();
            byte[] rndBytes = getRandomBytes();
            byte[] returnBytes = new byte[sigBytes.length + rndBytes.length];
            System.arraycopy(sigBytes, 0, returnBytes, 0, sigBytes.length);
            System.arraycopy(rndBytes, 0, returnBytes, sigBytes.length, rndBytes.length);
            return MessageDigest.getInstance("SHA-1").digest(returnBytes);
        } catch (java.lang.Exception e) {
            return null;
        }
    }
