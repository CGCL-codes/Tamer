    public static String getMD5Signature(TreeMap<String, String> params, String secret) {
        StringBuilder sign = new StringBuilder();
        Set<Entry<String, String>> paramSet = params.entrySet();
        StringBuilder query = new StringBuilder(secret);
        for (Entry<String, String> param : paramSet) {
            if (StringUtils.isNotEmpty(param.getKey()) && StringUtils.isNotEmpty(param.getValue())) {
                query.append(param.getKey()).append(param.getValue());
            }
        }
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(query.toString().getBytes("UTF-8"));
            for (int i = 0; i < bytes.length; i++) {
                String hex = Integer.toHexString(bytes[i] & 0xFF);
                if (hex.length() == 1) {
                    sign.append("0");
                }
                sign.append(hex.toUpperCase());
            }
        } catch (Exception e) {
            throw new java.lang.RuntimeException("Signature Generate Error!");
        }
        return sign.toString();
    }
