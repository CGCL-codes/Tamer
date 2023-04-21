    protected String createSignature(String secret, Map<String, String> params) {
        TreeSet<String> sortedSet = new TreeSet<String>(params.keySet());
        StringBuilder sig = new StringBuilder();
        sig.append(secret);
        for (String paramName : sortedSet) {
            sig.append(paramName);
            sig.append(params.get(paramName));
        }
        log.debug("building MD5 for: " + sig.toString());
        try {
            MessageDigest msgDigest = MessageDigest.getInstance("MD5");
            byte[] bytes = sig.toString().getBytes("UTF-8");
            msgDigest.update(bytes, 0, bytes.length);
            return getHexString(msgDigest.digest());
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }
