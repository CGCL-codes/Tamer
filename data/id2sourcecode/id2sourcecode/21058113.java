    public Identity authenticateIdentity(IdentityCredentials credentials) throws AccountNotFoundException, CredentialException {
        if (!(credentials instanceof NamePasswordCredentials)) {
            throw new CredentialException("unsupported credentials");
        }
        NamePasswordCredentials npc = (NamePasswordCredentials) credentials;
        String name = npc.getName();
        byte[] validPass = passwordMap.get(name);
        if (validPass == null) {
            throw new AccountNotFoundException("Unknown user: " + name);
        }
        byte[] pass = null;
        synchronized (digest) {
            digest.reset();
            try {
                pass = digest.digest((new String(npc.getPassword())).getBytes("UTF-8"));
            } catch (IOException ioe) {
                throw new CredentialException("Could not get password: " + ioe.getMessage());
            }
        }
        if (!Arrays.equals(validPass, pass)) {
            throw new CredentialException("Invalid credentials");
        }
        return new IdentityImpl(name);
    }
