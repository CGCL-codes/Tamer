    protected ContentManager.LocalChannelDefinition<?> createChannel(final ContentManager.ChannelGroupDefinition channelGroup, final FileableCmisObject[] path, final List<Tree<FileableCmisObject>> channelFolderDescendants, final ContentManager.ChannelSpecification<?> templateChannelSpecification, final SecurityContext securityContext, final HttpHeaders httpHeaders) throws WWWeeePortal.Exception {
        final Document channelIndexDocument = getChannelIndexDocument(channelFolderDescendants);
        if (channelIndexDocument == null) return null;
        path[5] = channelIndexDocument;
        final String channelID = channelGroup.getID() + '.' + path[4].getName();
        final ContentManager.LocalChannelSpecification<ProxyChannel> localChannelSpecification = new ContentManager.LocalChannelSpecification<ProxyChannel>(channelGroup, new RSProperties(this, channelGroup.getProperties()), channelID, null);
        final RSProperties channelProperties = new RSProperties(this, localChannelSpecification.getProperties());
        channelProperties.setProp(Channel.TITLE_TEXT_PROP, path[4].getName(), Locale.ROOT, null);
        addProperties(path[4], channelProperties);
        channelProperties.setProp(ProxyChannel.BASE_URI_PROP, resolveProxyChannelBaseURI(channelGroup.getContentContainer(), path, securityContext, httpHeaders), Locale.ROOT, null);
        final ContentManager.LocalChannelDefinition<ProxyChannel> channelDefinition;
        if ((templateChannelSpecification != null) && (templateChannelSpecification.getFirstChildDefinition() != null)) {
            @SuppressWarnings("unchecked") final ContentManager.LocalChannelDefinition<ProxyChannel> templateChannelDefinition = (ContentManager.LocalChannelDefinition<ProxyChannel>) templateChannelSpecification.getFirstChildDefinition();
            channelDefinition = templateChannelDefinition.duplicate(localChannelSpecification, channelProperties, true, null);
        } else {
            channelDefinition = new ContentManager.LocalChannelDefinition<ProxyChannel>(localChannelSpecification, channelProperties, null, ProxyChannel.class);
            final MimeType contentType = ConversionUtil.invokeConverter(IOUtil.STRING_MIME_TYPE_CONVERTER, channelIndexDocument.getContentStreamMimeType());
            if (HTMLUtil.isHTML(contentType)) {
                final RSProperties pluginProperties = new RSProperties(this, channelDefinition.getProperties());
                new ContentManager.ChannelPluginDefinition<ProxyChannelHTMLSource>(channelDefinition, pluginProperties, null, ProxyChannelHTMLSource.class);
            }
        }
        return channelDefinition;
    }
