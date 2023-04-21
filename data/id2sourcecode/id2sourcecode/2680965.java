    public static String sign(String[][] params) throws IOException {
        TreeMap<String, String> map = new TreeMap<String, String>();
        for (int i = 0; i < params.length; i++) {
            String key = params[i][0];
            String value = params[i][1];
            if (value != null) {
                map.put(key, value);
            }
        }
        String signature = "";
        Set<Map.Entry<String, String>> entrySet = map.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            signature += entry.getKey() + "=" + entry.getValue();
        }
        signature += getSecret();
        BigInteger bigInt;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] sum = md.digest(signature.getBytes("UTF-8"));
            bigInt = new BigInteger(1, sum);
        } catch (Exception ex) {
            throw new IOException(ex.getMessage());
        }
        return bigInt.toString(16);
    }
