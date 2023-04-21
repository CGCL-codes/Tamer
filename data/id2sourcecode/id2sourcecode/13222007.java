    protected boolean compareCredentials(DirContext context, User info, String credentials) throws NamingException {
        if (info == null || credentials == null) return (false);
        String password = info.password;
        if (password == null) return (false);
        boolean validated = false;
        if (hasMessageDigest()) {
            if (password.startsWith("{SHA}")) {
                synchronized (this) {
                    password = password.substring(5);
                    md.reset();
                    md.update(credentials.getBytes());
                    String digestedPassword = new String(Base64.encode(md.digest()));
                    validated = password.equals(digestedPassword);
                }
            } else {
                validated = (digest(credentials).equalsIgnoreCase(password));
            }
        } else validated = (digest(credentials).equals(password));
        return (validated);
    }
