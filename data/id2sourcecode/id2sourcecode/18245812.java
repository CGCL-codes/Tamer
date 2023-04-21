    public void testTitleNonPalindromeClosures() throws IOException {
        String in = "=title one== =title two== *bold*";
        ByteArrayInputStream is = new ByteArrayInputStream(in.getBytes());
        converter.parse(is);
        checkResult("<h1>title one</h1> <h1>title two</h1> <strong>bold</strong>");
    }
