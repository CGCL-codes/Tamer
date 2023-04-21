    private synchronized void load() throws DomHandlerException {
        URLConnection connection = null;
        InputStream is = null;
        try {
            connection = url.openConnection();
            long connectionModified = connection.getLastModified();
            if (connectionModified > this.lastModified) {
                this.lastModified = connectionModified;
                DocumentBuilder builder = factory.newDocumentBuilder();
                is = connection.getInputStream();
                if (is == null) {
                    throw new NullPointerException("Could not get an input stream for url:" + url.toExternalForm());
                }
                Document newDoc = builder.parse(is);
                this.document = newDoc;
            }
        } catch (IOException e) {
            throw new DomHandlerException(e);
        } catch (SAXException e) {
            throw new DomHandlerException(e);
        } catch (ParserConfigurationException e) {
            throw new DomHandlerException(e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
    }
