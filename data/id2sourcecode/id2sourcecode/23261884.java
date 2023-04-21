    public static BigInteger digest(String string) {
        byte[] data = string.getBytes();
        byte[] dig = getDigester().digest(data);
        BigInteger result = new BigInteger(dig);
        result = result.abs();
        return result;
    }
