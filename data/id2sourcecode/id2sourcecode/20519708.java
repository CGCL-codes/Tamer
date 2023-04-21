    public MonadUri getUri() throws ProgrammerException, UserException {
        return supply.getChannelsInstance().getUri().resolve(getUriId());
    }
