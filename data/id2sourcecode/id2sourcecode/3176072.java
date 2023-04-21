    @Override
    public STATUS checkAvailability(String isbn) {
        HttpGet get = null;
        try {
            HttpClient client = new DefaultHttpClient();
            get = new HttpGet(String.format(isbnSearchUrl, isbn));
            HttpResponse resp = client.execute(get);
            String link = (String) itemXpath.evaluate(new InputSource(resp.getEntity().getContent()), XPathConstants.STRING);
            if (link != null && !link.equals("")) {
                return STATUS.AVAILABLE;
            }
            return STATUS.NO_MATCH;
        } catch (Exception e) {
            Log.e(this.getClass().getName(), e.getMessage(), e);
            return null;
        } finally {
            if (get != null) {
                get.abort();
            }
        }
    }
