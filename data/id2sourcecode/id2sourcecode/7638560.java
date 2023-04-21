    @Test
    public void testAuthorizedList() throws Exception {
        final URL url = new URL("http://127.0.0.1:" + testPort + "/list?version=5");
        final HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        con.setRequestProperty("WWW-Authenticate", "Basic realm=\"karatasi\"");
        assertEquals("Expecting resource to exist.", HttpURLConnection.HTTP_OK, con.getResponseCode());
        assertTrue("mirror responds with Content-Type text/plain.", con.getContentType().matches("^(text|application)/xml.*"));
        assertNull("The server does not use any special encoding.", con.getContentEncoding());
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setCoalescing(true);
        dbf.setExpandEntityReferences(true);
        dbf.setIgnoringComments(true);
        dbf.setIgnoringElementContentWhitespace(true);
        dbf.setNamespaceAware(false);
        dbf.setValidating(true);
        dbf.setXIncludeAware(true);
        final DocumentBuilder db = dbf.newDocumentBuilder();
        db.setEntityResolver(new EntityResolver() {

            @Nullable
            public InputSource resolveEntity(@Nullable final String publicId, final String systemId) throws SAXException, IOException {
                if ("-//Karatasi//DTD Karatasi DB List 1.0//EN".equals(publicId) || "http://www.karatasi.org/DTD/karatasiDbList1.0.dtd".equals(systemId)) {
                    return new InputSource(new FileInputStream("src/prj/resources/karatasiDbList1.0.dtd"));
                }
                return null;
            }
        });
        db.parse(con.getInputStream(), url.toString());
    }
