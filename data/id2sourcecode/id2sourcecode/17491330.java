    private boolean isAuthorized(final UsernamePasswordClientCredential credential) {
        final boolean result;
        if (credential.getUsername() == null || credential.getUsername().isEmpty()) {
            result = false;
        } else {
            final Account a = userMap.get(credential.getUsername().toUpperCase());
            if (a == null) {
                if (LOG.isInfoEnabled()) {
                    LOG.info("No such user: " + credential.getUsername());
                }
                result = false;
            } else {
                String password = credential.getPassword() + a.getSalt();
                if (a.getDigest() != null) {
                    a.getDigest().reset();
                    password = new String(Hex.encode(a.getDigest().digest(password.getBytes())));
                }
                result = password.equals(a.getPassword());
            }
        }
        return result;
    }
