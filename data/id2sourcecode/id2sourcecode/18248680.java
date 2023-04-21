    private String parseQuery(String sUrl) {
        try {
            URL url = new URL(sUrl);
            DocumentBuilder nBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = nBuilder.parse(url.openStream());
            Element root = document.getDocumentElement();
            StringBuilder sBuilder = new StringBuilder();
            sBuilder.append("<html><body><p>");
            String word = getElementValue(root, "key");
            if (word != null) sBuilder.append(word);
            String pron = getElementValue(root, "pron");
            if (pron != null) sBuilder.append("&nbsp;&nbsp;[" + pron + "]");
            sBuilder.append("</p>");
            NodeList nodes = root.getElementsByTagName("def");
            if (nodes != null && nodes.getLength() > 0) {
                for (int i = 0; i < nodes.getLength(); i++) {
                    Element node = (Element) nodes.item(i);
                    String def = node.getTextContent();
                    sBuilder.append("<p>");
                    sBuilder.append(def);
                    sBuilder.append("</p>");
                }
            }
            nodes = root.getElementsByTagName("sent");
            if (nodes != null && nodes.getLength() > 0) {
                sBuilder.append("<p>Examples:</p>");
                for (int i = 0; i < nodes.getLength(); i++) {
                    Element node = (Element) nodes.item(i);
                    String orig = getElementValue(node, "orig");
                    sBuilder.append("<p>" + orig + "</p>");
                    String trans = getElementValue(node, "trans");
                    sBuilder.append("<p>" + trans + "</p>");
                }
            }
            sBuilder.append("</body></html>");
            return sBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
