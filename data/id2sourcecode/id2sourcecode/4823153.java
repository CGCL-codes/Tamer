    public Document getDocument(HttpUriRequest request) {
        SAXBuilder builder = new SAXBuilder();
        HttpResponse response = execute(request);
        if (isError) {
            return null;
        }
        Document doc = null;
        try {
            InputStream instream = response.getEntity().getContent();
            doc = builder.build(instream);
            instream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        }
        closeConnection();
        return doc;
    }
