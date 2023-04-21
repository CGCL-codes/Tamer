    private void contact(String uri) throws BookKeeprCommunicationException {
        try {
            URI hostUri = new URI(uri);
            synchronized (httpClient) {
                HttpGet req = new HttpGet(hostUri);
                HttpResponse resp = httpClient.execute(req);
                if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    try {
                        InputStream in = resp.getEntity().getContent();
                        XMLAble xmlable = XMLReader.read(in);
                        in.close();
                        if (xmlable instanceof BookkeeprHost) {
                            this.remoteHost = (BookkeeprHost) xmlable;
                            this.remoteHost.setUrl("http://" + hostUri.getHost() + ":" + hostUri.getPort());
                            status = "Connected (" + this.remoteHost.getStatus() + ")";
                        } else {
                            status = "Server Error";
                            throw new BookKeeprCommunicationException("BookKeepr returned the wrong thing for /ident");
                        }
                    } catch (SAXException ex) {
                        Logger.getLogger(BookKeeprConnection.class.getName()).log(Level.WARNING, "Got a malformed message from the bookkeepr", ex);
                        status = "Server Error";
                        throw new BookKeeprCommunicationException(ex);
                    }
                } else {
                    resp.getEntity().consumeContent();
                    status = "Server Error (" + resp.getStatusLine().getStatusCode() + ")";
                    throw new BookKeeprCommunicationException("Got a " + resp.getStatusLine().getStatusCode() + " from the BookKeepr");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(BookKeeprConnection.class.getName()).log(Level.WARNING, null, ex);
            status = "Server Error (IO error)";
            throw new BookKeeprCommunicationException(ex);
        } catch (HttpException ex) {
            Logger.getLogger(BookKeeprConnection.class.getName()).log(Level.WARNING, null, ex);
            status = "Server Error (HTTP error)";
            throw new BookKeeprCommunicationException(ex);
        } catch (URISyntaxException ex) {
            throw new BookKeeprCommunicationException(ex);
        }
    }
