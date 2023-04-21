    public boolean isNextWordInlineBrace(InlineStringReader reader, InlineStringWriter writer) throws IOException {
        int max = getInlineBraceWords()[0].length();
        for (String s : getInlineBraceWords()) {
            if (s.length() > max) max = s.length();
        }
        String next = (char) reader.getLastChar() + reader.readAheadSkipWhitespace(max + 1);
        for (String s : getInlineBraceWords()) {
            if (next.length() > s.length() + 1) {
                if (s.equals(next.substring(0, s.length()))) {
                    if (!Character.isLetterOrDigit(next.charAt(s.length()))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
