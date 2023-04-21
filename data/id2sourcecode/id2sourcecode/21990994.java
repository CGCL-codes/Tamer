    private Element loadDocument(URL url) {
        Document doc = null;
        try {
            InputSource xmlInp = new InputSource(url.openStream());
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder parser = docBuilderFactory.newDocumentBuilder();
            doc = parser.parse(xmlInp);
            Element root = doc.getDocumentElement();
            root.normalize();
            return root;
        } catch (SAXParseException err) {
            logger.error("ConfigFileSignOnDAO  ** Parsing error, line {}, uri {}", Integer.valueOf(err.getLineNumber()), err.getSystemId());
            logger.error("ConfigFileSignOnDAO  error: ", err);
        } catch (Exception pce) {
            logger.error("ConfigFileSignOnDAO  error: ", pce);
        }
        return null;
    }
