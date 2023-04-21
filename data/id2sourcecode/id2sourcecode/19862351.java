    public static XMLProblem loadProblem(String urlString) throws XMLParserException, MalformedURLException, IOException {
        URL url = new URL(urlString);
        InputStream urlStream = url.openStream();
        Reader reader = new InputStreamReader(urlStream);
        try {
            XMLReader xr = (XMLReader) Class.forName(DEFAULT_PARSER_NAME).newInstance();
            GenericHandler xh = new GenericHandler(xr, urlString);
            xr.setContentHandler(xh);
            IWPLog.info("[XMLProblemParser][load] about to parse!");
            xr.parse(new InputSource(reader));
            IWPLog.info("[XMLProblemParser][load] spitting out null");
            return xh.getProblem();
        } catch (ClassNotFoundException e) {
            IWPLog.x("[XMLProblemParser][load] ClassNotFoundException", e);
            throw new XMLParserException(e);
        } catch (InstantiationException e) {
            IWPLog.x("[XMLProblemParser][load] InstantationException", e);
            throw new XMLParserException(e);
        } catch (IllegalAccessException e) {
            IWPLog.x("[XMLProblemParser][load] IllegalAccessException", e);
            throw new XMLParserException(e);
        } catch (SAXException e) {
            IWPLog.x("[XMLProblemParser][load] SAXException", e);
            throw new XMLParserException(e);
        } catch (IOException e) {
            IWPLog.x("[XMLProblemParser][load] IOException", e);
            throw new XMLParserException(e);
        }
    }
