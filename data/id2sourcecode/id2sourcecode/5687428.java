    private String getDigestHexString(MessageDigest digester) {
        byte[] digest = digester.digest();
        int n = digest.length;
        String hex = "0123456789abcdef";
        char[] hexDigest = new char[n * 2];
        for (int i = 0; i < n; i++) {
            int b = digest[i] & 0x0FF;
            hexDigest[i * 2 + 0] = hex.charAt(b >>> 4);
            hexDigest[i * 2 + 1] = hex.charAt(b & 0x0f);
        }
        return new String(hexDigest);
    }
