    public HTTPParser(String url) {
        try {
            this.url = url;
            this.xmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            this.element = xmlDoc.createElement("root");
            this.element.setAttribute("LBL", "url");
            this.element.setAttribute("valid", "no");
            this.element.setAttribute("LNK", this.url);
            this.xmlDoc.appendChild(this.element);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
