    public Object callEx(String method, Object[] params) throws XMLRPCException {
        Object object = null;
        try {
            String body = methodCall(method, params);
            HttpEntity entity = new StringEntity(body);
            postMethod.setEntity(entity);
            HttpResponse response = client.execute(postMethod);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                throw new XMLRPCException("HTTP status code: " + statusCode + " != " + HttpStatus.SC_OK);
            }
            entity = response.getEntity();
            if (entity != null) {
                InputStream inStream = entity.getContent();
                try {
                    XmlPullParser pullParser = XmlPullParserFactory.newInstance().newPullParser();
                    pullParser.setInput(new BufferedInputStream(inStream), Helpers.UTF8_ENCODING);
                    pullParser.nextTag();
                    pullParser.require(XmlPullParser.START_TAG, null, Tag.METHOD_RESPONSE);
                    pullParser.nextTag();
                    String tag = pullParser.getName();
                    if (tag.equals(Tag.PARAMS)) {
                        pullParser.nextTag();
                        pullParser.require(XmlPullParser.START_TAG, null, Tag.PARAM);
                        pullParser.nextTag();
                        object = iXMLRPCSerializer.deserialize(pullParser);
                        postMethod.abort();
                    } else if (tag.equals(Tag.FAULT)) {
                        pullParser.nextTag();
                        object = iXMLRPCSerializer.deserialize(pullParser);
                        postMethod.abort();
                        if (object instanceof Map) {
                            @SuppressWarnings("unchecked") Map<String, Object> map = (Map<String, Object>) object;
                            String faultString = (String) map.get(Tag.FAULT_STRING);
                            int faultCode = (Integer) map.get(Tag.FAULT_CODE);
                            throw new XMLRPCFault(faultString, faultCode);
                        }
                        throw new XMLRPCException("Bad <fault> format in XMLRPC response");
                    } else {
                        postMethod.abort();
                        throw new XMLRPCException("Bad tag <" + tag + "> in XMLRPC response - neither <params> nor <fault>");
                    }
                } catch (IOException ex) {
                    throw ex;
                } catch (RuntimeException ex) {
                    postMethod.abort();
                    throw ex;
                } finally {
                    inStream.close();
                }
            }
        } catch (XMLRPCException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new XMLRPCException(e);
        }
        return object;
    }
