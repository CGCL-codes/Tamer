    public final synchronized void performRequest(final IWikiApiRequest war) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Executing: " + wikiPath + war.getExecutableQuery());
        }
        final HttpRequest req = new BasicHttpRequest(war.getRequestMethod(), wikiPath + war.getExecutableQuery());
        HttpEntity entity = null;
        try {
            final HttpResponse resp = this.client.execute(this.target, req);
            entity = resp.getEntity();
            final int statusCode = resp.getStatusLine().getStatusCode();
            war.setRequestResultStatus(statusCode);
            if (statusCode > HttpStatus.SC_OK) {
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error("Server Status: " + statusCode + " - " + resp.getStatusLine().getReasonPhrase());
                    LOGGER.error("Erroneous query: " + wikiPath + war.getExecutableQuery());
                }
                entity.consumeContent();
            } else {
                if (entity != null) {
                    this.inReader = new InputStreamReader(entity.getContent(), CHARSET);
                    if (this.inReader == null) {
                        war.setXMLResult(null);
                    } else {
                        if (!this.inReader.getEncoding().substring(0, 2).equalsIgnoreCase(CHARSET.substring(0, 2))) {
                            if (LOGGER.isErrorEnabled()) {
                                LOGGER.error("The request body is not properly encoded - found " + this.inReader.getEncoding());
                                LOGGER.error("Erroneous query: " + wikiPath + war.getExecutableQuery());
                            }
                        }
                        if (war.getFormat().equalsIgnoreCase("xml")) {
                            war.setXMLResult(XMLPreparer.getXMLRoot(this.inReader));
                        }
                    }
                }
            }
        } catch (ClientProtocolException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Request failed due to client protocol propblem.", e);
            }
        } catch (IOException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Request failed due to IO propblem.", e);
            }
        } finally {
            if (entity != null) {
                try {
                    entity.consumeContent();
                } catch (IOException e) {
                    if (LOGGER.isErrorEnabled()) {
                        LOGGER.error("Caught IO exception when gracefully cleaning up the mess from last failed request.");
                    }
                }
            }
        }
        war.processRequestResult();
    }
