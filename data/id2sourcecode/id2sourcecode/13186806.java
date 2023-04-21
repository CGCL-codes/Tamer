    private static synchronized Document getDoc(String url) {
        Document doc = null;
        try {
            doc = (Document) pools.get(url);
            if (doc == null) {
                InputStream is = new URL(url).openStream();
                doc = factory.newDocumentBuilder().parse(is);
                doc.normalize();
                doc.getDocumentElement().setAttribute(A_TYPE, V_FOLDER);
                is.close();
                pools.put(url, doc);
            }
        } catch (Exception e) {
        }
        return doc;
    }
