    public void testConverterNonPalindromeClosuers() throws IOException {
        String in = "aaaa _bb bb* ccc _dd dd* eeee";
        ByteArrayInputStream is = new ByteArrayInputStream(in.getBytes());
        converter.parse(is);
        checkResult("aaaa <i>bb bb<strong> ccc </i>dd dd</strong> eeee");
    }
