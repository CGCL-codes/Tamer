    @SuppressWarnings("unchecked")
    private static Element getChannelElement(URL url) {
        Document document = getDocument(url);
        if (document != null) {
            Element root = document.getRootElement();
            if (root != null) {
                for (Iterator i = root.elementIterator("channel"); i.hasNext(); ) {
                    Element item = (Element) i.next();
                    return item;
                }
            }
        }
        return null;
    }
