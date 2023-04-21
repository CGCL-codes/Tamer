    public Object filter(ExecutionContext context, DataElement container, Object o) {
        String s = String.valueOf(o);
        try {
            MessageDigest digest = MessageDigest.getInstance(digestType);
            byte[] result = digest.digest(s.getBytes("UTF-8"));
            BigInteger bi = new BigInteger(1, result);
            String val = bi.toString(16);
            while (val.length() < result.length * 2) val = "0" + val;
            return val;
        } catch (Exception e) {
            throw new RuntimeException("Error creating digest", e);
        }
    }
