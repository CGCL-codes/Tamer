    public InputStream sendXMLRequest(String body, boolean wrapBody) throws TAPIRException {
        if (!Encoding.isXML(encoding)) {
            throw new TAPIRException(this, "sendXMLRequest: invalid request");
        }
        String xml = (wrapBody) ? wrapXMLRequestBody(body, agentName, agentVersion) : body;
        lastRequest = xml;
        if (encoding == Encoding.XML_GET) {
            try {
                StringBuffer query = new StringBuffer(accessPoint);
                query.append("?request=");
                query.append(URLEncoder.encode(xml, UTF8));
                connection = (HttpURLConnection) (new URL(query.toString())).openConnection();
                if (agentName != null) {
                    connection.setRequestProperty("User-Agent", agentName);
                }
                connection.setDoOutput(true);
                connection.setRequestMethod("GET");
                connection.connect();
            } catch (Throwable t) {
                throw new TAPIRException(this, "sendXMLRequest [GET]", t);
            }
        } else {
            try {
                if (url == null) {
                    url = new URL(accessPoint);
                }
                connection = (HttpURLConnection) url.openConnection();
                if (agentName != null) {
                    connection.setRequestProperty("User-Agent", agentName);
                }
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                String request = null;
                if (encoding == Encoding.XML_RAW_POST) {
                    connection.setRequestProperty("Content-Type", "text/xml");
                } else if (encoding == Encoding.XML_POST) {
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    request = "request=";
                } else {
                    throw new TAPIRException(this, "sendXMLRequest: Incompatible TAPIR encoding to use with XML requests");
                }
                PrintStream out = new PrintStream(connection.getOutputStream());
                out.print((request == null) ? xml : request + xml);
                out.close();
            } catch (Throwable t) {
                throw new TAPIRException(this, "sendXMLRequest [POST]", t);
            }
        }
        return sendHTTPRequest(connection);
    }
