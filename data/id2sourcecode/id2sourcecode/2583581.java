    public URI rewriteProxiedFileLink(final Page.Request pageRequest, final URL proxiedFileURL, final URI linkURI, final boolean hyperLink, final boolean absoluteURLRequired) throws IllegalArgumentException, WWWeeePortal.Exception {
        if (proxiedFileURL == null) throw new IllegalArgumentException("null proxiedFileURL");
        if ((linkURI != null) && (linkURI.isOpaque())) {
            return linkURI;
        }
        final URL resolvedLinkURL;
        try {
            if (linkURI == null) {
                resolvedLinkURL = proxiedFileURL;
            } else if (linkURI.isAbsolute()) {
                resolvedLinkURL = linkURI.toURL();
            } else {
                resolvedLinkURL = new URL(proxiedFileURL, linkURI.toString());
            }
        } catch (MalformedURLException mue) {
            throw new ContentManager.ContentException("Error resolving proxied link URL", mue);
        }
        if (((hyperLink) && (isLinkRewritingHyperLinksToChannelDisabled(pageRequest))) || ((!hyperLink) && (!isLinkRewritingResourceLinksToChannelEnabled(pageRequest)))) {
            return rewriteProxiedFileLinkOutsideChannel(pageRequest, proxiedFileURL, linkURI, hyperLink, absoluteURLRequired, resolvedLinkURL);
        }
        if ((linkURI != null) && (linkURI.isAbsolute()) && (!equalHostAndPort(resolvedLinkURL, proxiedFileURL))) {
            return rewriteProxiedFileLinkOutsideChannel(pageRequest, proxiedFileURL, linkURI, hyperLink, absoluteURLRequired, resolvedLinkURL);
        }
        final String resolvedLinkPath = StringUtil.toString(StringUtil.mkNull(resolvedLinkURL.getPath(), false), "/");
        final URI baseURI = getProxiedBaseURI(pageRequest);
        final URI resolvedBaseURI;
        if (baseURI.isAbsolute()) {
            resolvedBaseURI = baseURI;
        } else {
            resolvedBaseURI = ConfigManager.getContextResourceLocalHostURI(pageRequest.getUriInfo(), baseURI.getPath(), ConversionUtil.invokeConverter(NetUtil.URI_QUERY_PARAMS_CONVERTER, baseURI), baseURI.getFragment(), true);
        }
        final String baseURIPath = resolvedBaseURI.getPath();
        final String baseURIFolder;
        if ((baseURIPath.length() == 1) || (baseURIPath.charAt(baseURIPath.length() - 1) == '/')) {
            baseURIFolder = baseURIPath;
        } else {
            final int lastSlashIndex = baseURIPath.lastIndexOf('/');
            baseURIFolder = (lastSlashIndex > 0) ? baseURIPath.substring(0, lastSlashIndex + 1) : String.valueOf('/');
        }
        if (!resolvedLinkPath.startsWith(baseURIFolder)) {
            return rewriteProxiedFileLinkOutsideChannel(pageRequest, proxiedFileURL, linkURI, hyperLink, absoluteURLRequired, resolvedLinkURL);
        }
        final String linkChannelLocalPath = StringUtil.mkNull(resolvedLinkPath.substring(baseURIFolder.length()), false);
        final String channelMode = ((hyperLink) && (!isMaximizationDisabled(pageRequest))) ? VIEW_MODE : RESOURCE_MODE;
        final ContentManager.ChannelSpecification<?> channelSpecification = pageRequest.getChannelSpecification(this);
        return channelSpecification.getKey().getChannelURI(pageRequest.getUriInfo(), channelMode, linkChannelLocalPath, ConversionUtil.invokeConverter(NetUtil.URI_QUERY_PARAMS_CONVERTER, linkURI), (linkURI != null) ? linkURI.getFragment() : null, absoluteURLRequired);
    }
