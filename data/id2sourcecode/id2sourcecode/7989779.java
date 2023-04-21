    public void copyFaultMessageToExchange(CamelMediationMessage fault, Exchange exchange) {
        String errorUrl = getChannel().getIdentityMediator().getErrorUrl();
        errorUrl = buildHttpTargetLocation(exchange.getIn(), new EndpointDescriptorImpl("idbus-error-page", "ErrorUIService", getBinding(), errorUrl, null), true);
        if (logger.isDebugEnabled()) logger.debug("Processing Fault Message " + fault.getMessageId() + ". Redirecting to " + errorUrl);
        Message httpOut = exchange.getOut();
        httpOut.getHeaders().put("Cache-Control", "no-cache, no-store");
        httpOut.getHeaders().put("Pragma", "no-cache");
        httpOut.getHeaders().put("Content-Type", "text/html");
        if (errorUrl != null) {
            if (logger.isDebugEnabled()) logger.debug("Configured error URL " + errorUrl + ".  Redirecting.");
            if (getChannel().getIdentityMediator() instanceof AbstractCamelMediator) {
                try {
                    AbstractCamelMediator mediator = (AbstractCamelMediator) getChannel().getIdentityMediator();
                    Artifact a = mediator.getArtifactQueueManager().pushMessage(fault.getMessage());
                    errorUrl += "?IDBusErrArt=" + a.getContent();
                    if (logger.isDebugEnabled()) logger.debug("Configured error URL " + errorUrl + ".  Redirecting.");
                    httpOut.getHeaders().put("http.responseCode", 302);
                    httpOut.getHeaders().put("Location", errorUrl);
                    return;
                } catch (Exception e) {
                    logger.error("Cannot forward error to error URL:" + errorUrl, e);
                }
            } else {
                httpOut.getHeaders().put("http.responseCode", 302);
                httpOut.getHeaders().put("Location", errorUrl);
                return;
            }
        }
        if (logger.isDebugEnabled()) logger.debug("No configured error URL. Generating error page.");
        httpOut.getHeaders().put("http.responseCode", 200);
        Html htmlErr = this.createHtmlErrorPage(fault.getMessage());
        try {
            String htmlStr = this.marshal(htmlErr, "http://www.w3.org/1999/xhtml", "html", new String[] { "org.w3._1999.xhtml" });
            ByteArrayInputStream baos = new ByteArrayInputStream(htmlStr.getBytes());
            httpOut.setBody(baos);
        } catch (Exception e) {
            logger.error("Cannot generate error page : " + e.getMessage(), e);
            httpOut.setBody("<html><body>Unhandled IDBus Error, verify log files for details!</body></html>");
        }
    }
