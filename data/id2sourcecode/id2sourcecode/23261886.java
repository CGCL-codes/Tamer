    public static BigInteger hash(String string) {
        byte[] data = string.getBytes();
        byte[] hashed = getDigester().digest(data);
        BigInteger dev = new BigInteger(hashed);
        dev = dev.abs();
        return (dev);
    }
