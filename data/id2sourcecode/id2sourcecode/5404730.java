    private Set<Element> querySolrEngine(String solrQuery) {
        HashSet<Element> elements = new HashSet<Element>();
        try {
            String encoded = baseQueryUrl + "/select?rows=" + getMaxRecords() + "&q=" + URLEncoder.encode(solrQuery, "UTF-8");
            URL url = new URL(encoded);
            InputStream inStream = url.openStream();
            Document doc = docBuild.parse(inStream);
            Element root = doc.getDocumentElement();
            NodeList recList = root.getElementsByTagName("doc");
            for (int i = 0; i < recList.getLength(); i++) {
                Element elem = (Element) recList.item(i);
                elements.add(elem);
            }
            inStream.close();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return elements;
    }
