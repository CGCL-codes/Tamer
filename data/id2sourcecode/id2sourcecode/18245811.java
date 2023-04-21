    public void testTitlePalindromeClosures() throws IOException {
        String in = "=title one= ==title two== *bold*";
        ByteArrayInputStream is = new ByteArrayInputStream(in.getBytes());
        converter.parse(is);
        checkResult("<h1>title one</h1> <h2>title two</h2> <strong>bold</strong>");
    }
