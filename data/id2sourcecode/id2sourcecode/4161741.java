    protected String generateSignature(final SortedParameters params) {
        StringBuilder sig = new StringBuilder(sharedSecret);
        for (String paramName : params.keySet()) {
            sig.append(paramName).append(params.get(paramName));
        }
        MessageDigest md5 = getMD5Digest();
        byte[] md5digest = md5.digest(sig.toString().getBytes());
        return convertToHexString(md5digest);
    }
