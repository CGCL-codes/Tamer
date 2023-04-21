    public void testConverterPalindromeClosuers() throws IOException {
        String in = "aaaa _bb bb_ ccc *dd dd* eeee";
        ByteArrayInputStream is = new ByteArrayInputStream(in.getBytes());
        converter.parse(is);
        checkResult("aaaa <i>bb bb</i> ccc <strong>dd dd</strong> eeee");
    }
