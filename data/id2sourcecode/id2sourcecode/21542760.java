    private static OBAA loadMetadataFromURL(URL url) {
        SAXParser parser = null;
        try {
            parser = SAXParserFactory.newInstance().newSAXParser();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        if (parser != null) {
            try {
                OBAA lom = new OBAA();
                GeneralReader generalReader = new GeneralReader();
                parser.parse(url.openStream(), new JColtraneXMLHandler(generalReader));
                General general = generalReader.getGeneral();
                if (general != null) lom.setGeneral(general);
                TechnicalReader technicalReader = new TechnicalReader();
                parser.parse(url.openStream(), new JColtraneXMLHandler(technicalReader));
                Technical technical = technicalReader.getTechnical();
                if (technical != null) {
                    lom.setTechnical(technical);
                }
                return lom;
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
