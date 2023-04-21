    public void testInvalidCSS() throws Exception {
        URL url = getClass().getResource("w3c.css");
        Reader reader = new InputStreamReader(url.openStream());
        StyleSheet sheet = parseStyleSheet(reader, "w3c.css");
        StyleSheetRenderer renderer = CSSStyleSheetRenderer.getSingleton();
        StringWriter writer = new StringWriter();
        RendererContext context = new RendererContext(writer, renderer);
        renderer.renderStyleSheet(sheet, context);
        String css = writer.toString();
        String results = diagnosticListener.getResults();
        System.out.println(results);
        assertEquals("a{color:red}\n" + "b{float:left}\n" + "f{color:green}\n" + "g{color:green}\n" + "h{color:green}\n" + "i{color:green}\n" + "j{color:green}\n" + "k{color:green}\n" + "l{color:green}", css);
    }
