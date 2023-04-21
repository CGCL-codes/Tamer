    public void open() throws MalformedURLException, IOException, SAXException {
        URL url = new URL(uri);
        URLConnection connection = url.openConnection();
        InputStream in = connection.getInputStream();
        body = convertStreamToString(in);
        body = body.replace("<head>", "<head><base href=\"http://" + ip + "\">");
        body = body.replace("document.login.username.focus();", "document.login.username.value = \"" + login + "\";" + "document.login.password.value = \"" + senha + "\";" + "doLogin();");
        in = new ByteArrayInputStream(body.getBytes("UTF-8"));
        Reader reader = new InputStreamReader(in);
        InputSource is = new InputSourceImpl(reader, uri);
        HtmlPanel htmlPanel = new HtmlPanel();
        UserAgentContext ucontext = new LocalUserAgentContext();
        HtmlRendererContext rendererContext = new LocalHtmlRendererContext(htmlPanel, ucontext);
        htmlPanel.setPreferredWidth(100);
        DocumentBuilderImpl builder = new DocumentBuilderImpl(rendererContext.getUserAgentContext(), rendererContext);
        Document document = builder.parse(is);
        in.close();
        htmlPanel.setDocument(document, rendererContext);
    }
