    public SigShare sign(final byte[] b) {
        final BigInteger x = (new BigInteger(b)).mod(n);
        final int randbits = n.bitLength() + 3 * ThreshUtil.L1;
        final BigInteger r = (new BigInteger(randbits, random));
        final BigInteger vprime = groupVerifier.modPow(r, n);
        final BigInteger xtilde = x.modPow(ThreshUtil.FOUR.multiply(delta), n);
        final BigInteger xprime = xtilde.modPow(r, n);
        BigInteger c = null;
        BigInteger z = null;
        try {
            md = MessageDigest.getInstance("SHA");
            md.reset();
            md.update(groupVerifier.mod(n).toByteArray());
            md.update(xtilde.toByteArray());
            md.update(verifier.mod(n).toByteArray());
            md.update(x.modPow(signVal, n).modPow(ThreshUtil.TWO, n).toByteArray());
            md.update(vprime.toByteArray());
            md.update(xprime.toByteArray());
            c = new BigInteger(md.digest()).mod(n);
            z = (c.multiply(secret)).add(r);
        } catch (final java.security.NoSuchAlgorithmException e) {
            debug("Provider could not locate SHA message digest .");
            e.printStackTrace();
        }
        final Verifier ver = new Verifier(z, c, verifier, groupVerifier);
        return new SigShare(id, x.modPow(signVal, n), ver);
    }
