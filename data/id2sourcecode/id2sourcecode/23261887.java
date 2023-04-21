    public static BigInteger hash(byte[] data) {
        byte[] hashed = getDigester().digest(data);
        BigInteger dev = new BigInteger(hashed);
        dev = dev.abs();
        return dev;
    }
