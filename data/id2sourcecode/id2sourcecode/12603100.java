    protected Channel.ViewResponse getCachedResponseOnException(final Page.Request pageRequest, final String mode, final WWWeeePortal.OperationalException wpoe) throws WWWeeePortal.Exception {
        if ((pageRequest.isMaximized(getChannel())) && (pageRequest.getEntity() != null)) return null;
        return getCachedResponse(pageRequest, mode);
    }
