    public BigInteger calculateHashSum() {
        return new BigInteger(1, digest.digest());
    }
