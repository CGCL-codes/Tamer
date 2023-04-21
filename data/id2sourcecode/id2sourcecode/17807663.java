    private String encode(String s) {
        String ret = "";
        try {
            byte[] b = digest.digest(s.getBytes("utf-8"));
            ret = new String((Base64.encodeBase64(b)), "utf-8").replace('/', '_');
        } catch (Exception e) {
        }
        return ret;
    }
