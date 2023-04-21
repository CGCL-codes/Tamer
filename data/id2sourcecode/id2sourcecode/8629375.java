    public TagDefinition define(String tag) {
        if (tag.startsWith("#")) {
            tag = tag.substring(1);
        }
        XMLStreamReader2 xmlStreamReader = null;
        try {
            BasicHttpParams params = new BasicHttpParams();
            DefaultHttpClient httpclient = new DefaultHttpClient(params);
            URI uri = URIUtils.createURI("http", "api.tagdef.com", -1, "/one." + tag, null, null);
            HttpGet httpget = new HttpGet(uri);
            if (logger.isDebugEnabled()) logger.debug("HttpGet.uri={}", httpget.getURI());
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                xmlStreamReader = (XMLStreamReader2) WstxInputFactory.newInstance().createXMLStreamReader(instream);
                while (xmlStreamReader.hasNext()) {
                    int type = xmlStreamReader.next();
                    if (type == XMLStreamConstants.START_ELEMENT) {
                        String tagName = xmlStreamReader.getName().getLocalPart();
                        if ("def".equals(tagName)) {
                            TagDefinition result = new TagDefinition();
                            result.setTag("#" + tag);
                            while (xmlStreamReader.hasNext()) {
                                type = xmlStreamReader.next();
                                if (type == XMLStreamConstants.START_ELEMENT) {
                                    tagName = xmlStreamReader.getName().getLocalPart();
                                    if ("text".equals(tagName)) {
                                        result.setDefinition(xmlStreamReader.getElementText());
                                    } else if ("uri".equals(tagName)) {
                                        result.setUrl(xmlStreamReader.getElementText());
                                    }
                                } else if (type == XMLStreamConstants.END_ELEMENT) {
                                    tagName = xmlStreamReader.getName().getLocalPart();
                                    if ("def".equals(tagName)) {
                                        break;
                                    }
                                }
                            }
                            return result;
                        }
                    }
                }
            }
        } catch (IOException ex) {
            if (logger.isWarnEnabled()) logger.warn("Exception!", ex);
        } catch (URISyntaxException ex) {
            if (logger.isWarnEnabled()) logger.warn("Exception!", ex);
        } catch (XMLStreamException ex) {
            if (logger.isWarnEnabled()) logger.warn("Exception!", ex);
        } finally {
            if (xmlStreamReader != null) {
                try {
                    xmlStreamReader.closeCompletely();
                } catch (XMLStreamException e) {
                }
            }
        }
        return null;
    }
