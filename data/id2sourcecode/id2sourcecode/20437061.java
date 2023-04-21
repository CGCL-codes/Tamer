    protected String digest(String credentials) {
        if (!hasMessageDigest()) {
            return credentials;
        }
        synchronized (this) {
            try {
                messageDigest.reset();
                messageDigest.update(credentials.getBytes());
                return HexUtils.convert(messageDigest.digest());
            } catch (Exception e) {
                log("realmBase.digest", e);
                return credentials;
            }
        }
    }
