    private synchronized String generateAuthToken(String name) {
        String clearText = name + oneTimeToken + secretKey;
        digest.reset();
        byte authToken[] = digest.digest(clearText.getBytes());
        return new String(authToken);
    }
