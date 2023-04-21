    public DesignPane() throws SAXException, IOException {
        super();
        JFrame testFrame;
        String t;
        String s;
        String uri = "http://www.w3schools.com";
        URL url = new URL(uri);
        URLConnection connection = url.openConnection();
        InputStream in = connection.getInputStream();
        Reader reader = new InputStreamReader(in);
        InputSource is = new InputSourceImpl(reader, uri);
        ucontext = new CobraConfig.LocalUserAgentContext();
        rendererContext = new CobraConfig.LocalHtmlRendererContext(this, ucontext);
        setPreferredWidth(800);
        DocumentBuilderImpl builder = new DocumentBuilderImpl(rendererContext.getUserAgentContext(), rendererContext);
        in.close();
    }
