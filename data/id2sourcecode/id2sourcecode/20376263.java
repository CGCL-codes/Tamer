    private String getSessionId(Credential credential, HttpServletRequest request) {
        md5Digester.reset();
        String aString = credential.getUsername();
        md5Digester.update(aString.getBytes(), 0, aString.length());
        return new BigInteger(1, md5Digester.digest()).toString(16);
    }
