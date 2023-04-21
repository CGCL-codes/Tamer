    static String generateSig(String url, String params, String secretKey) throws Exception {
        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
        url = url.replaceAll(FriendsterClient.API_SERVER, "");
        params = URLDecoder.decode(params.replaceAll("&", ""), "UTF-8");
        byte[] buffer = md.digest((url + params + secretKey).getBytes());
        return toHexString(buffer);
    }
