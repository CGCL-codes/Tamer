    public static XMLNoteDocument convert(URL url, XMLNoteImageIcon.Provider prov, XMLNoteStyles styles) throws IOException, ParserConfigurationException, SAXException, BadStyleException, BadDocumentException {
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        XHtmlToXMLNote cvt = new XHtmlToXMLNote(prov, styles);
        cvt.parse(in);
        return cvt.getDocument();
    }
