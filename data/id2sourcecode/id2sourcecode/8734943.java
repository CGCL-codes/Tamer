        @Override
        public void run() {
            InputStream stream = null;
            try {
                HttpPost post = new HttpPost(url);
                encodeFormParameters(post);
                HttpResponse response = httpClient.execute(post, context);
                HttpEntity entity = response.getEntity();
                long expected = entity.getContentLength();
                notifyClientTotalContentLength(expected);
                stream = entity.getContent();
                ResponseInputStream rs = new ResponseInputStream(stream);
                SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
                parser.parse(rs, new SAXHandler());
            } catch (SAXException ex) {
                if (cancel != true) {
                    notifyClientProcessingError(ex.getMessage(), ex);
                }
            } catch (IOException ex) {
                if (cancel != true) {
                    notifyClientProcessingError(ex.getMessage(), ex);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                if (client != null) {
                    notifyClientProcessingError(ex.getMessage(), ex);
                    client.requestComplete(null);
                }
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException ex) {
                        Logger.getLogger(XMLWebService.class.getName()).log(Level.WARNING, "Error closing http request stream for URL:" + url, ex);
                    }
                }
            }
        }
