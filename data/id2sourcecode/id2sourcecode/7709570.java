    public void copyMessageToExchange(CamelMediationMessage josso11Out, Exchange exchange) {
        MediationMessage out = josso11Out.getMessage();
        EndpointDescriptor ed = out.getDestination();
        assert ed != null : "Mediation Response MUST Provide a destination";
        if (logger.isDebugEnabled()) logger.debug("Creating JOSSO Artifact for location " + ed.getLocation());
        try {
            java.lang.Object msgValue = out.getContent();
            JossoMediator mediator = (JossoMediator) getChannel().getIdentityMediator();
            Message httpOut = exchange.getOut();
            Message httpIn = exchange.getIn();
            String artifactLocation = null;
            if (msgValue != null) {
                if (logger.isDebugEnabled()) logger.debug("Message Value found, storing artifact");
                LocalState lState = out.getState().getLocalState();
                if (lState != null) {
                    String key = lState.getAlternativeId("assertionId");
                    artifactLocation = this.buildHttpTargetLocation(httpIn, ed);
                    artifactLocation += (artifactLocation.contains("?") ? "&" : "?") + "josso_assertion_id=" + key + "&josso_artifact=" + out.getId() + "&josso_node=" + getConfigurationContext().getProperty("idbus.node");
                    lState.setValue(out.getId(), out);
                } else {
                    logger.error("Cannot store message value, no local state available!");
                }
            } else {
                if (logger.isDebugEnabled()) logger.debug("Message Value not found, ignoring artifact");
                artifactLocation = this.buildHttpTargetLocation(httpIn, ed);
            }
            if (logger.isDebugEnabled()) logger.debug("Redirecting with artifact to " + artifactLocation);
            copyBackState(out.getState(), exchange);
            if (!isEnableAjax()) {
                httpOut.getHeaders().put("Cache-Control", "no-cache, no-store");
                httpOut.getHeaders().put("Pragma", "no-cache");
                httpOut.getHeaders().put("http.responseCode", 302);
                httpOut.getHeaders().put("Content-Type", "text/html");
                httpOut.getHeaders().put("Location", artifactLocation);
            } else {
                Html redir = this.createHtmlArtifactMessage(artifactLocation);
                String marshalledHttpResponseBody = XmlUtils.marshal(redir, "http://www.w3.org/1999/xhtml", "html", new String[] { "org.w3._1999.xhtml" });
                marshalledHttpResponseBody = marshalledHttpResponseBody.replace("&amp;josso_", "&josso_");
                httpOut.getHeaders().put("Cache-Control", "no-cache, no-store");
                httpOut.getHeaders().put("Pragma", "no-cache");
                httpOut.getHeaders().put("http.responseCode", 200);
                httpOut.getHeaders().put("Content-Type", "text/html");
                ByteArrayInputStream baos = new ByteArrayInputStream(marshalledHttpResponseBody.getBytes());
                httpOut.setBody(baos);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
