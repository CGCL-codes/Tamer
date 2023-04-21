    protected URI getProxiedFileLocalURI(final Page.Request pageRequest, final String mode, final boolean validate) throws WWWeeePortal.Exception, WebApplicationException {
        final URI channelLocalPath = pageRequest.getChannelLocalPath(this);
        final Object[] context = new Object[] { channelLocalPath };
        URI proxiedFileLocalURI = pluginValueHook(PROXIED_FILE_LOCAL_URI_HOOK, context, pageRequest);
        if (proxiedFileLocalURI == null) {
            final URI proxiedFilePath;
            if (channelLocalPath != null) {
                if ((validate) && (isDefaultPathRestrictionEnabled(pageRequest))) {
                    final URI proxiedFilePathDefault = getProxiedFilePathDefault(pageRequest);
                    if (!channelLocalPath.equals(proxiedFilePathDefault)) {
                        throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity("Request outside default path").type("text/plain").build());
                    }
                }
                if ((validate) && (!isParentFoldersRestrictionDisabled(pageRequest))) {
                    if (!isRelativeSubPath(channelLocalPath.getPath())) {
                        throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity("Request outside base URL folder").type("text/plain").build());
                    }
                }
                proxiedFilePath = channelLocalPath;
            } else if (VIEW_MODE.equals(mode)) {
                proxiedFilePath = getProxiedFilePathDefault(pageRequest);
            } else {
                proxiedFilePath = null;
            }
            final Map<String, List<String>> reqParameters = (pageRequest.isMaximized(this)) ? pageRequest.getUriInfo().getQueryParameters() : null;
            if ((validate) && (isDefaultPathRestrictionEnabled(pageRequest)) && (reqParameters != null) && (!reqParameters.isEmpty())) {
                throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity("Request outside default path").type("text/plain").build());
            }
            if ((proxiedFilePath == null) && ((reqParameters == null) || (reqParameters.isEmpty()))) return null;
            final StringBuffer proxiedFileLocalURIBuffer = (proxiedFilePath != null) ? new StringBuffer(proxiedFilePath.toString()) : new StringBuffer();
            if ((reqParameters != null) && (!reqParameters.isEmpty())) {
                for (String reqParamName : reqParameters.keySet()) {
                    final List<String> reqParamValues = reqParameters.get(reqParamName);
                    if (reqParamValues == null) continue;
                    final String reqParamNameEncoded = ConversionUtil.invokeConverter(NetUtil.URL_ENCODE_CONVERTER, reqParamName);
                    for (String reqParamValue : reqParamValues) {
                        if (proxiedFileLocalURIBuffer.indexOf("?") >= 0) {
                            proxiedFileLocalURIBuffer.append('&');
                        } else {
                            proxiedFileLocalURIBuffer.append('?');
                        }
                        proxiedFileLocalURIBuffer.append(reqParamNameEncoded);
                        proxiedFileLocalURIBuffer.append('=');
                        proxiedFileLocalURIBuffer.append(ConversionUtil.invokeConverter(NetUtil.URL_ENCODE_CONVERTER, reqParamValue));
                    }
                }
            }
            try {
                proxiedFileLocalURI = new URI(proxiedFileLocalURIBuffer.toString());
            } catch (URISyntaxException urise) {
                throw new WWWeeePortal.SoftwareException(urise);
            }
        }
        proxiedFileLocalURI = pluginFilterHook(PROXIED_FILE_LOCAL_URI_HOOK, context, pageRequest, proxiedFileLocalURI);
        return proxiedFileLocalURI;
    }
