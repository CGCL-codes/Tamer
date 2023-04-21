    public ActionResponse service() throws IOException, UPNPResponseException {
        ActionResponse rtrVal = null;
        UPNPResponseException upnpEx = null;
        IOException ioEx = null;
        StringBuffer body = new StringBuffer(256);
        body.append("<?xml version=\"1.0\"?>\r\n");
        body.append("<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\"");
        body.append(" s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">");
        body.append("<s:Body>");
        body.append("<u:").append(serviceAction.getName()).append(" xmlns:u=\"").append(service.getServiceType()).append("\">");
        if (serviceAction.getInputActionArguments() != null) {
            for (Iterator itr = inputParameters.iterator(); itr.hasNext(); ) {
                InputParamContainer container = (InputParamContainer) itr.next();
                body.append("<").append(container.name).append(">").append(container.value);
                body.append("</").append(container.name).append(">");
            }
        }
        body.append("</u:").append(serviceAction.getName()).append(">");
        body.append("</s:Body>");
        body.append("</s:Envelope>");
        if (log.isDebugEnabled()) log.debug("POST prepared for URL " + service.getControlURL());
        URL url = new URL(service.getControlURL().toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        HttpURLConnection.setFollowRedirects(false);
        conn.setRequestProperty("HOST", url.getHost() + ":" + url.getPort());
        conn.setRequestProperty("CONTENT-TYPE", "text/xml; charset=\"utf-8\"");
        conn.setRequestProperty("CONTENT-LENGTH", Integer.toString(body.length()));
        conn.setRequestProperty("SOAPACTION", "\"" + service.getServiceType() + "#" + serviceAction.getName() + "\"");
        OutputStream out = conn.getOutputStream();
        out.write(body.toString().getBytes());
        out.flush();
        out.close();
        conn.connect();
        InputStream input = null;
        if (log.isDebugEnabled()) log.debug("executing query :\n" + body);
        try {
            input = conn.getInputStream();
        } catch (IOException ex) {
            input = conn.getErrorStream();
        }
        if (input != null) {
            int response = conn.getResponseCode();
            String responseBody = getResponseBody(input);
            if (log.isDebugEnabled()) log.debug("received response :\n" + responseBody);
            SAXParserFactory saxParFact = SAXParserFactory.newInstance();
            saxParFact.setValidating(false);
            saxParFact.setNamespaceAware(true);
            ActionMessageResponseParser msgParser = new ActionMessageResponseParser(serviceAction);
            StringReader stringReader = new StringReader(responseBody);
            InputSource src = new InputSource(stringReader);
            try {
                SAXParser parser = saxParFact.newSAXParser();
                parser.parse(src, msgParser);
            } catch (ParserConfigurationException confEx) {
                throw new RuntimeException("ParserConfigurationException during SAX parser creation, please check your env settings:" + confEx.getMessage());
            } catch (SAXException saxEx) {
                upnpEx = new UPNPResponseException(899, saxEx.getMessage());
            } finally {
                try {
                    input.close();
                } catch (IOException ex) {
                }
            }
            if (upnpEx == null) {
                if (response == HttpURLConnection.HTTP_OK) {
                    rtrVal = msgParser.getActionResponse();
                } else if (response == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                    upnpEx = msgParser.getUPNPResponseException();
                } else {
                    ioEx = new IOException("Unexpected server HTTP response:" + response);
                }
            }
        }
        try {
            out.close();
        } catch (IOException ex) {
        }
        conn.disconnect();
        if (upnpEx != null) {
            throw upnpEx;
        }
        if (rtrVal == null && ioEx == null) {
            ioEx = new IOException("Unable to receive a response from the UPNP device");
        }
        if (ioEx != null) {
            throw ioEx;
        }
        return rtrVal;
    }
