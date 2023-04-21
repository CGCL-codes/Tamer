    public void sign(Random r, DSAPrivateKey sk, DSAGroup g) {
        setPublicKey(new DSAPublicKey(g, sk));
        Digest ctx = SHA1.getInstance();
        hashUpdate(ctx, new String[] { "Signature" });
        BigInteger k = Util.generateLargeRandom(80, 160, r);
        BigInteger m = Util.byteArrayToMPI(ctx.digest());
        setSignature(DSA.sign(g, sk, k, m));
    }
