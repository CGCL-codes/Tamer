    @Override
    public String digest() {
        return String.format("%0" + (md.getDigestLength() * 2) + "x", new BigInteger(1, md.digest()));
    }
