    @Override
    public BookJB getInfo(String isbn) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("q", isbn));
        try {
            URI uri = URIUtils.createURI("http", "books.google.com", -1, "/books/feeds/volumes", URLEncodedUtils.format(params, "UTF-8"), null);
            HttpResponse resp = client.execute(new HttpGet(uri));
            Node bookNode = (Node) entityXpath.evaluate(new InputSource(resp.getEntity().getContent()), XPathConstants.NODE);
            NodeList titleNodes = (NodeList) titleXpath.evaluate(bookNode, XPathConstants.NODESET);
            String title = concatNodes(titleNodes, ": ");
            NodeList authorNodes = (NodeList) authorXpath.evaluate(bookNode, XPathConstants.NODESET);
            String author = concatNodes(authorNodes, ", ");
            String volumeId = (String) volumeIdXpath.evaluate(bookNode, XPathConstants.STRING);
            Log.d(this.getClass().getName(), "volumeId: " + volumeId);
            String thumbUrl = (String) thumbXpath.evaluate(bookNode, XPathConstants.STRING);
            thumbUrl = thumbUrl.replaceFirst("&edge(=[^&]*)?(?=&|$)|^edge(=[^&]*)?(&|$)", "");
            BookJB retVal = new BookJB(isbn, volumeId, title, author);
            retVal.setThumbUrl(thumbUrl);
            return retVal;
        } catch (Exception e) {
            Log.e(this.getClass().getName(), e.getMessage(), e);
            return null;
        }
    }
