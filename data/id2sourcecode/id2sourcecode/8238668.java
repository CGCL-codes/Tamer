    private boolean doesntActuallyNeedWhitespaceBeforeCss(InlineStringReader reader, InlineStringWriter writer, int cur) throws IOException {
        return cur == '(' || cur == ')' || cur == '}' || cur == ';' || cur == '.' || cur == ',' || cur == '[' || cur == ']' || cur == '+' || cur == '-' || cur == ':';
    }
