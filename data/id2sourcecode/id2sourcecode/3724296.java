    public HtmlCollection(String url) throws InvalidUrlException {
        try {
            parentUrlPart = getParentUrlPart(url);
            page = new BufferedReader(new InputStreamReader(openStream(url)));
            page = new BufferedReader(new InputStreamReader(openStream(url)));
            colors = new ArrayList();
        } catch (Exception e) {
            throw new InvalidUrlException(url, e);
        }
    }
