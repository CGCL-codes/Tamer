    public static void main(String[] args) throws Exception {
        Logger.getLogger("org.lobobrowser").setLevel(Level.WARNING);
        String uri = "http://lobobrowser.org/browser/home.jsp";
        uri = "http://www.google.com/fusiontables/embedviz?viz=MAP&q=select+col4%2C+col5%2C+col6%2C+col7%2C+col11%2C+col8%2C+col9%2C+col10%2C+col13+from+1159987+&h=false&lat=46.55886030311719&lng=-95.80078125&z=4&t=2&l=col13";
        URL url = new URL(uri);
        URLConnection connection = url.openConnection();
        InputStream in = connection.getInputStream();
        Reader reader = new InputStreamReader(in);
        InputSource is = new InputSourceImpl(reader, uri);
        HtmlPanel htmlPanel = new HtmlPanel();
        UserAgentContext ucontext = new LocalUserAgentContext();
        HtmlRendererContext rendererContext = new LocalHtmlRendererContext(htmlPanel, ucontext);
        htmlPanel.setPreferredWidth(800);
        DocumentBuilderImpl builder = new DocumentBuilderImpl(rendererContext.getUserAgentContext(), rendererContext);
        Document document = builder.parse(is);
        in.close();
        htmlPanel.setDocument(document, rendererContext);
        final JFrame frame = new JFrame();
        frame.getContentPane().add(htmlPanel);
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
