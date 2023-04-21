    private String makeRequest(String action, final Map<String, String> params) throws Exception {
        final Map<String, String> linkedMap = new LinkedHashMap<String, String>();
        linkedMap.put("user", user);
        for (String p : params.keySet()) {
            linkedMap.put(p, params.get(p));
        }
        if (this.plainKey) {
            linkedMap.put("apikey", this.apiKey);
        } else {
            String tosign = this.user + StringUtils.join(params.values(), "") + this.apiKey;
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            String sign = convertToHex(md5.digest(convertToHex(sha1.digest(tosign.getBytes())).getBytes()));
            linkedMap.put("sign", sign);
        }
        return processor.process(action, linkedMap);
    }
