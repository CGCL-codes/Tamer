    protected String digest(String credentials) {
        if (hasMessageDigest() == false) return (credentials);
        synchronized (this) {
            try {
                md.reset();
                md.update(credentials.getBytes());
                return (Tools.convert(md.digest()));
            } catch (Exception e) {
                log("realmBase.digest", e);
                return (credentials);
            }
        }
    }
