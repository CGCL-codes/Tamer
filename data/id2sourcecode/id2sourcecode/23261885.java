    public static BigInteger digest(byte[] data) {
        byte[] dig = getDigester().digest(data);
        BigInteger result = new BigInteger(dig);
        result = result.abs();
        return result;
    }
