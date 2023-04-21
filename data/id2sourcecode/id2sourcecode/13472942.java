    public String unshorten(String url) {
        XMLStreamReader2 xmlStreamReader = null;
        try {
            List<NameValuePair> qparams = new ArrayList<NameValuePair>();
            qparams.add(new BasicNameValuePair("url", url));
            BasicHttpParams params = new BasicHttpParams();
            DefaultHttpClient httpclient = new DefaultHttpClient(params);
            URI uri = URIUtils.createURI("http", "realurl.org", -1, "/api/v1/getrealurl.php", URLEncodedUtils.format(qparams, "UTF-8"), null);
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
                        if ("real".equals(xmlStreamReader.getName().getLocalPart())) {
                            return xmlStreamReader.getElementText();
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
