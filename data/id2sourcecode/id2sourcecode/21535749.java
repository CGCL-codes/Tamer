    public static double h(String guid, byte[] processHash) {
        md.reset();
        md.update(guid.getBytes());
        byte[] hash = md.digest(processHash);
        BigInteger x = new BigInteger(hash);
        if (x.signum() == -1) {
            x = MAX_VALUE_INTEGER.add(x);
        }
        return new BigDecimal(x).divide(MAX_VALUE_DECIMAL).doubleValue();
    }
