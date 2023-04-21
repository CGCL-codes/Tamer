    public static String sign(Map<String, String> protocal, Map<String, String> parameter, String secret) throws IOException {
        Map<String, String> sortedParams = new TreeMap<String, String>(protocal);
        sortedParams.putAll(protocal);
        Set<Entry<String, String>> paramSet = sortedParams.entrySet();
        StringBuilder query = new StringBuilder(secret);
        for (Entry<String, String> param : paramSet) {
            if (areNotEmpty(param.getKey(), param.getValue())) {
                query.append(param.getKey()).append(param.getValue());
            }
        }
        MessageDigest md5 = getMd5MessageDigest();
        byte[] bytes = md5.digest(query.toString().getBytes("utf-8"));
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }
