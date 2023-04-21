    public BigInteger getDigestInteger(byte[] expression) {
        dig.reset();
        dig.update(expression);
        return new BigInteger(dig.digest());
    }
