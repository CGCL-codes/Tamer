    private String randomVerificationString(byte[] sigBytes, byte[] rndBytes, boolean validString) throws Exception {
        try {
            if (validString) {
                if (sigBytes == null || rndBytes == null) throw new Exception(" sigBytes or rndBytes is null. ");
                byte[] returnBytes = new byte[sigBytes.length + rndBytes.length];
                System.arraycopy(sigBytes, 0, returnBytes, 0, sigBytes.length);
                System.arraycopy(rndBytes, 0, returnBytes, sigBytes.length, rndBytes.length);
                return Base64.encode(MessageDigest.getInstance("SHA-1").digest(returnBytes));
            }
        } catch (java.lang.Exception e) {
        }
        byte[] retArray = new byte[20];
        new java.security.SecureRandom().nextBytes(retArray);
        return Base64.encode(retArray);
    }
