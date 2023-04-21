    protected ContentManager.ChannelSpecification<?> getRequestedChannel(final String ownerID, final String pageGroupID, final String pageID, final String channelID, final String clientUserLogin, final String[] clientUserRoles) throws WWWeeePortal.Exception, WebApplicationException {
        final ContentManager.PageDefinition<?> pageDefinition = wwweeeDB.getPage(new ContentManager.PageDefinition.Key(ownerID, pageGroupID, pageID), true, true, clientUserLogin, clientUserRoles, properties, true);
        if (pageDefinition == null) throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).build());
        final ContentManager.ChannelSpecification<?> channelSpecification = pageDefinition.getChannelSpecification(channelID);
        if (channelSpecification == null) throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).build());
        return channelSpecification;
    }
