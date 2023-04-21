    public Identity authenticateIdentity(IdentityCredentials credentials) throws AccountNotFoundException, CredentialException {
        if (!(credentials instanceof NamePasswordCredentials)) throw new CredentialException("unsupported credentials");
        NamePasswordCredentials npc = (NamePasswordCredentials) credentials;
        String name = npc.getName();
        byte[] validPass = passwordMap.get(name);
        if (validPass == null) {
            byte[] pass;
            synchronized (digest) {
                digest.reset();
                try {
                    pass = digest.digest((new String(npc.getPassword())).getBytes("UTF-8"));
                } catch (IOException e) {
                    throw new CredentialException("Could not calculate " + "new account password: " + e.getMessage());
                }
            }
            passwordMap.put(name, pass);
            try {
                FileOutputStream out = new FileOutputStream(passwordFile);
                out.write(name.getBytes("UTF-8"));
                out.write("\t".getBytes("UTF-8"));
                out.write(NamePasswordAuthenticator.encodeBytes(pass));
                out.write("\n".getBytes("UTF-8"));
                out.close();
            } catch (IOException e) {
                throw new CredentialException("Could not save new account " + "credentials: " + e.getMessage());
            }
            return new IdentityImpl(name);
        }
        byte[] pass;
        synchronized (digest) {
            digest.reset();
            try {
                pass = digest.digest((new String(npc.getPassword())).getBytes("UTF-8"));
            } catch (IOException ioe) {
                throw new CredentialException("Could not get password: " + ioe.getMessage());
            }
        }
        if (!Arrays.equals(validPass, pass)) throw new CredentialException("Invalid credentials");
        return new IdentityImpl(name);
    }
