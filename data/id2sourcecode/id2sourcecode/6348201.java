        private void performOn(HttpClient client) {
            try {
                HttpResponse sponse = client.execute(request);
                HttpEntity entity = sponse.getEntity();
                if (entity != null) {
                    Document doc = builder.parse(entity.getContent());
                    if (requestType == RequestType.DOI) {
                        Unixref unixref = new Unixref(doc);
                        String ownerPrefix = unixref.getOwnerPrefix();
                        handler.onMetadata(doi, unixref);
                        if (!ownerPrefix.isEmpty()) {
                            queuePubReq(doi, handler, unixref.getOwnerPrefix());
                        } else {
                            handler.onComplete(doi);
                        }
                    } else if (requestType == RequestType.PUBLISHER) {
                        Publisher publisher = new Publisher(doc);
                        handler.onPublisher(doi, publisher);
                        handler.onComplete(doi);
                    }
                } else {
                    StatusLine sl = sponse.getStatusLine();
                    handler.onFailure(doi, sl.getStatusCode(), sl.getReasonPhrase());
                }
            } catch (ClientProtocolException e) {
                handler.onFailure(doi, CLIENT_EXCEPTION_CODE, e.toString());
            } catch (IOException e) {
                handler.onFailure(doi, CLIENT_EXCEPTION_CODE, e.toString());
            } catch (SAXException e) {
                handler.onFailure(doi, CRUMMY_XML_CODE, e.toString());
            } catch (XPathExpressionException e) {
                handler.onFailure(doi, BAD_XPATH_CODE, e.toString());
            }
        }
