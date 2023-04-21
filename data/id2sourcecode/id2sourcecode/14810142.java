    private Metadata executeMethod(HttpUriRequest method) throws MbXMLException, WebServiceException {
        HttpParams params = new BasicHttpParams();
        HttpProtocolParamBean paramsBean = new HttpProtocolParamBean(params);
        paramsBean.setUserAgent(USERAGENT);
        method.setParams(params);
        try {
            System.out.println("Hitting url: " + method.getURI().toString());
            HttpResponse response = this.httpClient.execute(method);
            lastHitTime = System.currentTimeMillis();
            int statusCode = response.getStatusLine().getStatusCode();
            switch(statusCode) {
                case HttpStatus.SC_SERVICE_UNAVAILABLE:
                    {
                        log.warn(buildMessage(response, "Service unavaillable"));
                        method.abort();
                        lastHitTime = System.currentTimeMillis();
                        wait(1);
                        return null;
                    }
                case HttpStatus.SC_OK:
                    InputStream instream = response.getEntity().getContent();
                    Metadata mtd = getParser().parse(instream);
                    try {
                        instream.close();
                    } catch (Exception ignore) {
                    }
                    lastHitTime = System.currentTimeMillis();
                    return mtd;
                case HttpStatus.SC_NOT_FOUND:
                    throw new ResourceNotFoundException(buildMessage(response, "Not found"));
                case HttpStatus.SC_BAD_REQUEST:
                    throw new RequestException(buildMessage(response, "Bad Request"));
                case HttpStatus.SC_FORBIDDEN:
                    throw new AuthorizationException(buildMessage(response, "Forbidden"));
                case HttpStatus.SC_UNAUTHORIZED:
                    throw new AuthorizationException(buildMessage(response, "Unauthorized"));
                case HttpStatus.SC_INTERNAL_SERVER_ERROR:
                    {
                        throw new AuthorizationException(buildMessage(response, "Internal server error"));
                    }
                default:
                    {
                        String em = buildMessage(response, "");
                        log.error("Fatal web service error: " + em);
                        throw new WebServiceException(em);
                    }
            }
        } catch (IOException e) {
            log.error("Fatal transport error: " + e.getMessage());
            throw new WebServiceException(e.getMessage(), e);
        }
    }
