    public boolean verifies() {
        Digest ctx = SHA1.getInstance();
        hashUpdate(ctx, new String[] { "Signature" });
        BigInteger m = Util.byteArrayToMPI(ctx.digest());
        return DSA.verify(getPublicKey(), getSignature(), m);
    }
