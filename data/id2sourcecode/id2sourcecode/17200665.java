    private String computeHybiAcceptValue(String websocketKey) throws NoSuchAlgorithmException {
        String concatenatedKey = websocketKey + WEBSOCKET_ACCEPT_HYBI_10_PARAMETER;
        byte[] result = MessageDigest.getInstance("SHA").digest(concatenatedKey.getBytes());
        result = Base64.encodeBase64(result);
        return new String(result);
    }
