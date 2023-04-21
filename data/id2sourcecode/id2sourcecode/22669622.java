    public Updater(String updateURL) throws Exception {
        available = false;
        version = null;
        location = null;
        InputStream stream = null;
        try {
            DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();
            DocumentBuilder constructeur = fabrique.newDocumentBuilder();
            constructeur.setErrorHandler(new ErrorHandler() {

                @Override
                public void warning(SAXParseException exception) throws SAXException {
                }

                @Override
                public void fatalError(SAXParseException exception) throws SAXException {
                }

                @Override
                public void error(SAXParseException exception) throws SAXException {
                }
            });
            URL url = new URL(updateURL.replaceAll("\\{version\\}", java.net.URLEncoder.encode(CoreConfig.getString("ProductInfo.version"), "UTF-8")));
            stream = url.openConnection(ENTDownloader.getInstance().getProxy()).getInputStream();
            xmlUpdateInformation = constructeur.parse(stream);
            Element racine = xmlUpdateInformation.getDocumentElement();
            NodeList liste = racine.getElementsByTagName("NeedToBeUpdated");
            if (liste.getLength() != 0) {
                Element e = (Element) liste.item(0);
                if (e.getTextContent().equals("yes")) {
                    available = true;
                }
            }
        } catch (Exception e) {
            try {
                stream.close();
            } catch (Exception e1) {
            }
            throw e;
        }
    }
