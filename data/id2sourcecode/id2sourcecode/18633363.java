    public String digest(String credentials) {
        if (md == null) {
            return credentials;
        }
        synchronized (this) {
            try {
                md.reset();
                md.update(credentials.getBytes());
                return (HexUtils.convert(md.digest()));
            } catch (Exception e) {
                return credentials;
            }
        }
    }
