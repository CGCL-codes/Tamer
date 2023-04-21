    public String digest(String message) {
        return Base64.encodeBase64String(mac.digest(Base64.decodeBase64(message)));
    }
