    private String getCodeSignature(Object oTarget) {
        try {
            byte[] abCode = Base.findClassResource(oTarget.getClass());
            oDigest.update(abCode);
            byte[] abRes = oDigest.digest();
            return new java.math.BigInteger(abRes).toString(32);
        } catch (IOException oExc) {
            Base.println("Warning: MarketPlace unable to load code (to digest).");
            return "";
        }
    }
