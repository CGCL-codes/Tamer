    protected byte[] getDigestedZZ(String otherPublicKeyBase64) {
        DHPublicKey dhPublicKey = stringToPublicKey(otherPublicKeyBase64);
        DHPrivateKey dhPrivateKey = getPrivateKey();
        BigInteger xa = dhPrivateKey.getX();
        BigInteger yb = dhPublicKey.getY();
        BigInteger p = _dhParameterSpec.getP();
        BigInteger zz = yb.modPow(xa, p);
        return _hDigest.digest(zz.toByteArray());
    }
