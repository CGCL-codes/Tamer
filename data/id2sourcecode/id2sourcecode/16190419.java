    private HtmlPanel initBrowser() throws IOException, SAXException {
        String uri = helpUri;
        URL url = new URL(uri);
        URLConnection connection = url.openConnection();
        InputStream in = connection.getInputStream();
        Reader reader = new InputStreamReader(in);
        InputSource is = new InputSourceImpl(reader, uri);
        HtmlPanel htmlPanel = new HtmlPanel();
        HtmlRendererContext rendererContext = new LocalHtmlRendererContext(htmlPanel);
        htmlPanel.setPreferredWidth(800);
        DocumentBuilderImpl builder = new DocumentBuilderImpl(rendererContext.getUserAgentContext(), rendererContext);
        Document document = builder.parse(is);
        in.close();
        htmlPanel.setDocument(document, rendererContext);
        return htmlPanel;
    }
