    public String read(EnumSensorType sensorType, Map<String, String> stateMap) {
        String urlPath = "/rest/nodes/";
        String preIsy99Cmd = "/";
        String urlStr = null;
        DefaultHttpClient httpclient = new DefaultHttpClient();
        InputStream content = null;
        String value = null;
        try {
            httpclient.getCredentialsProvider().setCredentials(new AuthScope(host, 80), new UsernamePasswordCredentials(userName, password));
            urlStr = "http://" + host + urlPath + address + preIsy99Cmd;
            HttpGet httpget = new HttpGet(urlStr);
            log.debug("executing request " + httpget.getURI());
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (response.getStatusLine().getStatusCode() != 200) {
                log.debug("status line " + response.getStatusLine());
                if (entity != null) {
                    log.debug("Response content length: " + entity.getContentLength());
                }
            } else {
                log.debug("Command was sent successfull ");
            }
            log.debug("----------------------------------------");
            log.debug("----------------------------------------");
            SAXBuilder builder = new SAXBuilder();
            content = response.getEntity().getContent();
            Document document = (Document) builder.build(content);
            Element rootNode = document.getRootElement();
            @SuppressWarnings("unchecked") List<Element> list = rootNode.getChildren("node");
            for (Element node : list) {
                log.debug("XML Parsing ");
                log.debug("address : " + node.getChildText("address"));
                log.debug("name : " + node.getChildText("name"));
                log.debug("type: " + node.getChildText("type"));
                log.debug("enabled: " + node.getChildText("enabled"));
                log.debug("elk_id: " + node.getChildText("ELK_ID"));
                log.debug("property: " + node.getChildText("property"));
                value = node.getChild("property").getAttributeValue("value");
                log.debug("prop->value-> " + value);
            }
        } catch (IOException ioe) {
            log.error("IOException while reading data from ISY-99", ioe);
            return "";
        } catch (JDOMException jdomex) {
            log.error("error while parsing response from ISY-99", jdomex);
            return "";
        } finally {
            try {
                content.close();
            } catch (IOException e) {
                ;
            }
        }
        int integerValue = -1;
        try {
            integerValue = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.error("invalid sensor reading from ISY-99: expected an integer, got \"" + value + "\"");
            return "";
        }
        switch(sensorType) {
            case SWITCH:
                if (value == null) {
                    return "off";
                } else if (integerValue >= 1) {
                    return "on";
                } else if (integerValue == 0) {
                    return "off";
                }
            case LEVEL:
                return "" + (integerValue * (100 / 250));
            case RANGE:
                return "" + integerValue;
            default:
                return "";
        }
    }
