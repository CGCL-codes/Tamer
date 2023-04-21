    private String generateRandomPW(String password) {
        java.security.MessageDigest firstHash = null;
        try {
            firstHash = java.security.MessageDigest.getInstance("SHA1");
        } catch (Exception e2) {
            if (debugLevel >= SAWSConstant.ErrorInfo) sawsDebugLog.write(e2.toString());
        }
        byte[] sawsHash = null;
        String temp = password;
        for (int i = 0; i < 10; ++i) {
            sawsHash = firstHash.digest(temp.getBytes());
            temp = new String(sawsHash);
        }
        String pw = new String(Base64.encode(sawsHash));
        return pw;
    }
