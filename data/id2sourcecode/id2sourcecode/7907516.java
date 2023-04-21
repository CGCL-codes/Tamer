    public static int readTill(boolean isCommentStart, char limitChar, InputStream source, OutputStream out, boolean dontSaveComments) throws IOException {
        int nextChar = -1;
        int lastChar = -1;
        while ((nextChar = source.read()) != -1) {
            if (!isCommentStart && nextChar == '/') {
                if (!dontSaveComments) out.write(nextChar);
                isCommentStart = true;
                lastChar = nextChar;
                continue;
            }
            if (isCommentStart && nextChar == '/') {
                if (!dontSaveComments) out.write(nextChar);
                while ((nextChar = source.read()) != -1 && nextChar != '\n') if (!dontSaveComments) out.write(nextChar);
                if (!dontSaveComments) out.write(nextChar); else out.write(LINE_BREAK);
                isCommentStart = false;
                lastChar = nextChar;
            } else if (isCommentStart && nextChar == '*') {
                if (!dontSaveComments) out.write(nextChar);
                while ((nextChar = source.read()) != -1) {
                    if (!dontSaveComments) out.write(nextChar);
                    if (nextChar == '*' && (nextChar = source.read()) == '/') break;
                }
                if (!dontSaveComments) out.write(nextChar); else out.write(' ');
                isCommentStart = false;
                lastChar = nextChar;
            } else if (nextChar == '"' && lastChar != '\\' && limitChar != '"') {
                out.write(nextChar);
                while ((nextChar = source.read()) != -1 && (nextChar != '"' || (nextChar == '"' && lastChar == '\\'))) {
                    out.write(nextChar);
                    lastChar = nextChar;
                }
                if (nextChar != -1) out.write(nextChar);
                lastChar = nextChar;
            } else if (nextChar != limitChar) {
                out.write(nextChar);
                lastChar = nextChar;
            } else {
                lastChar = nextChar;
                break;
            }
        }
        return nextChar;
    }
