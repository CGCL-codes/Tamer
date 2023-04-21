    protected HttpContext validateProxyResponse(final HttpContext proxyContext, final Page.Request pageRequest, final String mode) throws WWWeeePortal.Exception, WebApplicationException {
        final HttpResponse proxyResponse = (HttpResponse) proxyContext.getAttribute(ExecutionContext.HTTP_RESPONSE);
        final int responseCode = proxyResponse.getStatusLine().getStatusCode();
        if (responseCode == Response.Status.OK.getStatusCode()) return proxyContext;
        try {
            if (responseCode == Response.Status.NOT_MODIFIED.getStatusCode()) {
                throw new WebApplicationException(Response.Status.NOT_MODIFIED);
            } else if (((responseCode >= Response.Status.MOVED_PERMANENTLY.getStatusCode()) && (responseCode <= Response.Status.SEE_OTHER.getStatusCode())) || (responseCode == Response.Status.TEMPORARY_REDIRECT.getStatusCode())) {
                if (pageRequest.isMaximized(this)) {
                    final String location = ConversionUtil.invokeConverter(HTTPUtil.HEADER_VALUE_CONVERTER, proxyResponse.getLastHeader("Location"));
                    final URI locationURI;
                    try {
                        locationURI = new URI(location);
                    } catch (URISyntaxException urise) {
                        throw new ContentManager.ContentException("Error parsing 'Location' header: " + location, urise);
                    }
                    final URL proxiedFileURL;
                    try {
                        proxiedFileURL = HTTPUtil.getRequestTargetURL(proxyContext);
                    } catch (URISyntaxException urise) {
                        throw new ContentManager.ContentException("Error parsing proxied file URL", urise);
                    } catch (MalformedURLException mue) {
                        throw new ContentManager.ContentException("Error parsing proxied file URL", mue);
                    }
                    final URI fixedLocation;
                    try {
                        fixedLocation = rewriteProxiedFileLink(pageRequest, proxiedFileURL, locationURI, VIEW_MODE.equals(mode), true);
                    } catch (ContentManager.ContentException wpce) {
                        throw new ContentManager.ContentException("Error rewriting 'Location' header", wpce);
                    }
                    throw new WebApplicationException(Response.status(RESTUtil.Response.Status.fromStatusCode(responseCode)).location(fixedLocation).build());
                }
            } else if (responseCode == Response.Status.UNAUTHORIZED.getStatusCode()) {
                if (pageRequest.isMaximized(this)) {
                    throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED).header("WWW-Authenticate", ConversionUtil.invokeConverter(HTTPUtil.HEADER_VALUE_CONVERTER, proxyResponse.getLastHeader("WWW-Authenticate"))).build());
                }
            } else if ((responseCode == Response.Status.NOT_FOUND.getStatusCode()) || (responseCode == Response.Status.GONE.getStatusCode())) {
                final URI channelLocalPath = pageRequest.getChannelLocalPath(this);
                if (channelLocalPath != null) {
                    throw new WebApplicationException(Response.Status.fromStatusCode(responseCode));
                }
            } else if (responseCode == RESTUtil.Response.Status.METHOD_NOT_ALLOWED.getStatusCode()) {
                final URI channelLocalPath = pageRequest.getChannelLocalPath(this);
                if (channelLocalPath != null) {
                    throw new WebApplicationException(Response.status(RESTUtil.Response.Status.METHOD_NOT_ALLOWED).header("Allow", ConversionUtil.invokeConverter(HTTPUtil.HEADER_VALUE_CONVERTER, proxyResponse.getLastHeader("Allow"))).build());
                }
            } else if (responseCode == RESTUtil.Response.Status.REQUEST_TIMEOUT.getStatusCode()) {
                throw new WWWeeePortal.OperationalException(new WebApplicationException(responseCode));
            } else if (responseCode == Response.Status.BAD_REQUEST.getStatusCode()) {
                throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).build());
            } else if ((responseCode >= 400) && (responseCode < 500)) {
                if (pageRequest.isMaximized(this)) {
                    throw new WebApplicationException(Response.status(RESTUtil.Response.Status.fromStatusCode(responseCode)).build());
                }
            } else if ((responseCode >= RESTUtil.Response.Status.BAD_GATEWAY.getStatusCode()) && (responseCode <= RESTUtil.Response.Status.GATEWAY_TIMEOUT.getStatusCode())) {
                throw new WWWeeePortal.OperationalException(new WebApplicationException(responseCode));
            }
            final String codePhrase = (RESTUtil.Response.Status.fromStatusCode(responseCode) != null) ? " (" + RESTUtil.Response.Status.fromStatusCode(responseCode).getReasonPhrase() + ")" : "";
            final String responsePhrase = (proxyResponse.getStatusLine().getReasonPhrase() != null) ? ": " + proxyResponse.getStatusLine().getReasonPhrase() : "";
            final ConfigManager.ConfigException configurationException = new ConfigManager.ConfigException("The proxied file server returned code '" + responseCode + "'" + codePhrase + responsePhrase, null);
            if (getLogger().isLoggable(Level.FINE)) {
                try {
                    final Reader reader = createProxiedFileReader(proxyResponse, getProxyResponseHeader(pageRequest, proxyResponse, "Content-Type", HTTPUtil.HEADER_MIME_TYPE_CONVERTER));
                    final String responseContent = ConversionUtil.invokeConverter(IOUtil.READER_STRING_CONVERTER, reader);
                    LogAnnotation.annotate(configurationException, "ProxiedFileResponseContent", responseContent, Level.FINE, false);
                } catch (Exception e) {
                }
            }
            throw configurationException;
        } catch (WWWeeePortal.Exception wpe) {
            try {
                LogAnnotation.annotate(wpe, "ProxiedFileURL", HTTPUtil.getRequestTargetURL(proxyContext), null, false);
            } catch (Exception e) {
            }
            LogAnnotation.annotate(wpe, "ProxiedFileResponseCode", Integer.valueOf(responseCode), null, false);
            LogAnnotation.annotate(wpe, "ProxiedFileResponseCodeReasonPhrase", RESTUtil.Response.Status.fromStatusCode(responseCode), null, false);
            LogAnnotation.annotate(wpe, "ProxiedFileResponseReasonPhrase", proxyResponse.getStatusLine().getReasonPhrase(), null, false);
            throw wpe;
        }
    }
