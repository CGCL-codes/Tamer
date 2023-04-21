    private HttpApiResponse execute(String method, HttpApiRequest httpApiRequest, BaseRequestItem requestItem) {
        if (httpApiRequest.href == null) {
            throw new ProtocolException(HttpServletResponse.SC_BAD_REQUEST, "href parameter is missing");
        }
        Uri href = normalizeUrl(httpApiRequest.href);
        try {
            HttpRequest req = new HttpRequest(href);
            req.setMethod(method);
            if (httpApiRequest.body != null) {
                req.setPostBody(httpApiRequest.body.getBytes());
            }
            for (Map.Entry<String, List<String>> header : httpApiRequest.headers.entrySet()) {
                if (!BAD_HEADERS.contains(header.getKey().trim().toUpperCase())) {
                    for (String value : header.getValue()) {
                        req.addHeader(header.getKey(), value);
                    }
                }
            }
            Uri gadgetUri = getGadgetUri(requestItem.getToken(), httpApiRequest);
            if (gadgetUri == null) {
                throw new ProtocolException(HttpServletResponse.SC_BAD_REQUEST, "Gadget URI not specified in request");
            }
            req.setGadget(gadgetUri);
            if (httpApiRequest.authz != null) {
                req.setAuthType(AuthType.parse(httpApiRequest.authz));
            }
            if (req.getAuthType() != AuthType.NONE) {
                req.setSecurityToken(requestItem.getToken());
                Map<String, String> authSettings = getAuthSettings(requestItem);
                OAuthArguments oauthArgs = new OAuthArguments(req.getAuthType(), authSettings);
                oauthArgs.setSignOwner(httpApiRequest.signOwner);
                oauthArgs.setSignViewer(httpApiRequest.signViewer);
                req.setOAuthArguments(oauthArgs);
            }
            req.setIgnoreCache(httpApiRequest.noCache);
            req.setSanitizationRequested(httpApiRequest.sanitize);
            if (httpApiRequest.refreshInterval != null) {
                req.setCacheTtl(httpApiRequest.refreshInterval);
            }
            HttpResponse results = requestPipeline.execute(req);
            results = contentRewriterRegistry.rewriteHttpResponse(req, results);
            HttpApiResponse httpApiResponse = new HttpApiResponse(results, transformBody(httpApiRequest, results), httpApiRequest);
            if (requestItem.getToken() != null) {
                String updatedAuthToken = requestItem.getToken().getUpdatedToken();
                if (updatedAuthToken != null) {
                    httpApiResponse.token = updatedAuthToken;
                }
            }
            return httpApiResponse;
        } catch (GadgetException ge) {
            throw new ProtocolException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ge.getMessage(), ge);
        } catch (RewritingException re) {
            throw new ProtocolException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, re.getMessage(), re);
        }
    }
