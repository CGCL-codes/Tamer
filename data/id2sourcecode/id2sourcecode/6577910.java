    public void setURL(URL url) {
        this.url = url;
        try {
            docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = docBuilder.parse(url.openStream());
        } catch (javax.xml.parsers.ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }
