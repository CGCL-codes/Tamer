    @Test
    public void test1() throws IOException, SAXException {
        String string = "<?xml version=\"1.0\"?>\n" + "<html>\n" + "<a name=\"anchor1\"/><h1>chapter1</h1>\n" + "<a name=\"anchor1.1\"/><h2>section1.1</h2>\n" + "<a name=\"anchor1.2\"/><h2>section1.2</h2>\n" + "<a name=\"anchor1.2.1\"/><h3>section1.2.1</h3>\n" + "<a name=\"anchor1.2.2\"/><h3>section1.2.2</h3>\n" + "<a href=\"to-be-ingored\">link</a>\n" + "<a name=\"anchor2\"/><h1>chapter2</h1>\n" + "<a name=\"anchor2.1\"/><h2>section2.1</h2>\n" + "<a name=\"anchor2.2\"/><h2>section2.2</h2>\n" + "</html>\n";
        StringWriter sw = new StringWriter();
        PrintWriter writer = new PrintWriter(sw);
        Reader reader = new StringReader(string);
        new Parse(writer).parse("filename", reader);
        writer.close();
        String result = sw.toString();
        String expected = "" + "<div class=\"toc-1\"><span class=\"toc-1-nr\">1</span><a href=\"filename#anchor1\">chapter1</a></div>\n" + "<div class=\"toc-2\"><span class=\"toc-2-nr\">1.1</span><a href=\"filename#anchor1.1\">section1.1</a></div>\n" + "<div class=\"toc-2\"><span class=\"toc-2-nr\">1.2</span><a href=\"filename#anchor1.2\">section1.2</a></div>\n" + "<div class=\"toc-3\"><span class=\"toc-3-nr\">1.2.1</span><a href=\"filename#anchor1.2.1\">section1.2.1</a></div>\n" + "<div class=\"toc-3\"><span class=\"toc-3-nr\">1.2.2</span><a href=\"filename#anchor1.2.2\">section1.2.2</a></div>\n" + "<div class=\"toc-1\"><span class=\"toc-1-nr\">2</span><a href=\"filename#anchor2\">chapter2</a></div>\n" + "<div class=\"toc-2\"><span class=\"toc-2-nr\">2.1</span><a href=\"filename#anchor2.1\">section2.1</a></div>\n" + "<div class=\"toc-2\"><span class=\"toc-2-nr\">2.2</span><a href=\"filename#anchor2.2\">section2.2</a></div>\n";
        System.out.println(result);
        assertEquals(result, expected);
    }
