    private String queryServer(String fullUrl, String tag) {
        String result = null;
        try {
            URL url = new URL(fullUrl);
            DataInputStream istream = new DataInputStream(url.openStream());
            Document doc = docBuild.parse(istream);
            Element root = doc.getDocumentElement();
            NodeList nodes = root.getElementsByTagName(tag);
            if (nodes != null && nodes.getLength() > 0) {
                Element resElem = (Element) nodes.item(0);
                result = resElem.getTextContent().trim();
            }
            istream.close();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
