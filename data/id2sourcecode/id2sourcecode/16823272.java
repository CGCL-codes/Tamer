    @SuppressWarnings("unchecked")
    public Object callWithBody(String url, String body) throws XMLRPCException {
        postMethod.setURI(URI.create(url));
        try {
            HttpEntity entity = new StringEntity(body);
            postMethod.setEntity(entity);
            HttpResponse response = client.execute(postMethod);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                throw new XMLRPCException("HTTP status code: " + statusCode + " != " + HttpStatus.SC_OK);
            }
            XmlPullParser pullParser = XmlPullParserFactory.newInstance().newPullParser();
            entity = response.getEntity();
            Reader reader = new InputStreamReader(new BufferedInputStream(entity.getContent()));
            pullParser.setInput(reader);
            pullParser.nextTag();
            pullParser.require(XmlPullParser.START_TAG, null, Tag.METHOD_RESPONSE);
            pullParser.nextTag();
            String tag = pullParser.getName();
            if (tag.equals(Tag.PARAMS)) {
                pullParser.nextTag();
                pullParser.require(XmlPullParser.START_TAG, null, Tag.PARAM);
                pullParser.nextTag();
                Object obj = iXMLRPCSerializer.deserialize(pullParser);
                entity.consumeContent();
                return obj;
            } else if (tag.equals(Tag.FAULT)) {
                pullParser.nextTag();
                Map<String, Object> map = (Map<String, Object>) iXMLRPCSerializer.deserialize(pullParser);
                String faultString = (String) map.get(Tag.FAULT_STRING);
                int faultCode = (Integer) map.get(Tag.FAULT_CODE);
                entity.consumeContent();
                throw new XMLRPCFault(faultString, faultCode);
            } else {
                entity.consumeContent();
                throw new XMLRPCException("Bad tag <" + tag + "> in XMLRPC response - neither <params> nor <fault>");
            }
        } catch (XMLRPCException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new XMLRPCException(e);
        }
    }
