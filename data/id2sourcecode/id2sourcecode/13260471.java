    @Override
    public void actionPerformed() throws ActionException {
        if (getLog().isDebugEnabled()) {
            getLog().debug("Logging in " + myCredentials.getUsername());
        }
        try {
            User user = myAuthenticator.authenticate(myCredentials);
            getChannel(CHANNEL_USER).setValue(user);
        } catch (AuthenticationException e) {
            throw new ActionException(e.getMessage(), e.getCause());
        }
    }
