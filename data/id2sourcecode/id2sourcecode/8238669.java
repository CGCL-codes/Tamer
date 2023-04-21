    private boolean needsWhitespaceBetweenCss(InlineStringReader reader, InlineStringWriter writer, int a, int b) throws IOException {
        return (a == ',') || (a == '+') || (a == '<') || (b == '+') || (b == '<') || (a == '>') || (b == '>') || (b == '.') || (b == '{') || (a == ':');
    }
