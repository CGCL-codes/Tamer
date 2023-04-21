    public String readXml(String systemId) throws Exception {
        String xml = "";
        this.systemId = Val.chkStr(systemId);
        this.baseUrl = "";
        this.agsServiceUrls = new ArrayList<String>();
        boolean isHttp = false;
        try {
            URL url = new URL(systemId);
            String tmp = url.getProtocol();
            isHttp = (tmp != null) && (tmp.equals("http") || tmp.equals("https"));
        } catch (MalformedURLException e) {
            isHttp = false;
        }
        if (isHttp) {
            this.baseUrl = this.systemId;
            int nIdx = this.baseUrl.indexOf("?");
            if (nIdx != -1) this.baseUrl = this.baseUrl.substring(0, nIdx);
            boolean tryAgsFirst = false;
            if (this.baseUrl.length() > 0) {
                String lc = this.systemId.toLowerCase();
                if (lc.indexOf("/rest/") != -1) {
                    if (lc.indexOf("service=") == -1) tryAgsFirst = true;
                }
            }
            HttpClientRequest client = HttpClientRequest.newRequest();
            client.setUrl(this.systemId);
            if (tryAgsFirst) {
                try {
                    xml = this.readAgsXml(this.systemId);
                } catch (Exception e) {
                    if (this.wasJson) {
                        throw e;
                    } else {
                        String result = client.readResponseAsCharacters();
                        StringReader reader = new StringReader(result);
                        StringWriter writer = new StringWriter();
                        transform(new StreamSource(reader), new StreamResult(writer));
                        xml = Val.chkStr(writer.toString());
                    }
                }
            } else {
                try {
                    String result = client.readResponseAsCharacters();
                    StringReader reader = new StringReader(result);
                    StringWriter writer = new StringWriter();
                    transform(new StreamSource(reader), new StreamResult(writer));
                    xml = Val.chkStr(writer.toString());
                } catch (TransformerException te) {
                    try {
                        xml = this.readAgsXml(this.systemId);
                    } catch (MalformedURLException mue) {
                        throw te;
                    } catch (JSONException e) {
                        throw te;
                    }
                }
            }
        } else {
            StringWriter writer = new StringWriter();
            transform(new StreamSource(this.systemId), new StreamResult(writer));
            xml = Val.chkStr(writer.toString());
        }
        return xml;
    }
