    @Override
    public final void run() {
        HttpEntity entity = null;
        InputStreamReader inReader = null;
        try {
            final HttpResponse response = httpClient.execute(this.target, this.httpget, this.context);
            entity = response.getEntity();
            final int statusCode = response.getStatusLine().getStatusCode();
            this.iwar.setRequestResultStatus(statusCode);
            if (statusCode > HttpStatus.SC_OK) {
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error("Server Status: " + statusCode + " - " + response.getStatusLine().getReasonPhrase());
                }
                entity.consumeContent();
            } else {
                if (entity != null) {
                    inReader = new InputStreamReader(entity.getContent());
                    if (inReader == null) {
                        this.iwar.setXMLResult(null);
                    } else {
                        if (!inReader.getEncoding().substring(0, 2).equalsIgnoreCase(ThreadedMWApiBot.CHARSET.substring(0, 2))) {
                            if (LOGGER.isErrorEnabled()) {
                                LOGGER.error("The request body is not properly encoded - found " + inReader.getEncoding());
                            }
                        }
                        if (this.iwar.getFormat().equalsIgnoreCase("xml")) {
                            this.iwar.setXMLResult(XMLPreparer.getXMLRoot(inReader));
                        }
                    }
                }
            }
        } catch (ClientProtocolException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Request failed due to client protocol problem. Query was: " + this.iwar.getExecutableQuery(), e);
            }
            httpget.abort();
        } catch (IOException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Request failed due to IO problem. Query was: " + this.iwar.getExecutableQuery(), e);
            }
            httpget.abort();
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
        this.iwar.processRequestResult();
    }
