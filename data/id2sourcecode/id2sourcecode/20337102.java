    public Extension build(String url) throws IOException, SAXException {
        HttpGet get = new HttpGet(url);
        try {
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream is = entity.getContent();
                try {
                    Extension e = build(is);
                    LOG.info("Successfully parsed extension: " + e.getTitle());
                    return e;
                } catch (SAXException e) {
                    LOG.error("Unable to parse XML for extension: " + e.getMessage(), e);
                } finally {
                    is.close();
                }
                entity.consumeContent();
            }
        } catch (Exception e) {
            LOG.error(e);
        }
        return null;
    }
