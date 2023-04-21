    private boolean doesntActuallyNeedWhitespaceBeforePhp(InlineStringReader reader, InlineStringWriter writer, int cur) throws IOException {
        return cur == '(' || cur == ')' || cur == '}' || cur == ';' || cur == '[' || cur == ']' || cur == ',' || cur == '+' || cur == '-' || writer.getPrevious() == '-' || writer.getPrevious() == '+' || isTwoCharacterOperator(cur, reader.readAhead()) || (writer.getLastWritten(2).equals("::")) || (writer.getLastWritten(2).equals("->"));
    }
