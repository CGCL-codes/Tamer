    public void ladeAvatarPack(String filename, URL baseurl) throws AvatarException {
        DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
        DocumentBuilder parser;
        Document doc;
        URL url, dtdurl;
        fac.setValidating(true);
        try {
            parser = fac.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            throw new AvatarException("Kann Parser nicht erstellen!", ex);
        }
        if (baseurl == null) {
            url = this.getClass().getResource(filename);
        } else {
            try {
                url = new URL(baseurl.toExternalForm() + filename);
            } catch (MalformedURLException ex) {
                throw new AvatarException("Kann URL des Avatarfiles nicht erstellen!", ex);
            }
        }
        dtdurl = this.getClass().getResource("/fkkavatarpack.dtd");
        try {
            doc = parser.parse(url.openStream(), dtdurl.toExternalForm());
        } catch (SAXException ex) {
            throw new AvatarException("Fehler im Avatarpack : fehlerhaftes XML-Dokument!", ex);
        } catch (IOException ex) {
            throw new AvatarException("Kann Avatarpack " + url + " nicht laden!");
        }
        Element docele = doc.getDocumentElement();
        if (!docele.getNodeName().equals("avatarpack")) {
            throw new AvatarException("Fehler im Avatarpack : falsche Document Node!");
        }
        NodeList avalist = docele.getChildNodes();
        for (int i = 0; i < avalist.getLength(); ++i) {
            ladeAvatar(avalist.item(i), baseurl);
        }
    }
