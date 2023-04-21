    public static String Signature(Map<String, String> params, String secret) throws Exception {
        String result = "";
        try {
            String top_appkey = params.get("top_appkey");
            String top_parameters = params.get("top_parameters");
            String top_session = params.get("top_session");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest((top_appkey + top_parameters + top_session + secret).getBytes());
            BASE64Encoder encode = new BASE64Encoder();
            result = encode.encode(digest);
        } catch (Exception ex) {
            throw new Exception("Sign Error !");
        }
        return result;
    }
