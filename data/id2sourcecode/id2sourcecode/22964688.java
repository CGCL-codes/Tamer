    public static void replaceTemplateVariables(Writer writer, Reader reader, Map<String, String> variables) throws IOException {
        StreamTokenizer stk = new StreamTokenizer(reader);
        stk.resetSyntax();
        stk.wordChars(0, Integer.MAX_VALUE);
        stk.ordinaryChar('$');
        stk.ordinaryChar('{');
        stk.ordinaryChar('}');
        stk.ordinaryChar('\n');
        String variableName = null;
        StringBuffer tmpBuf = new StringBuffer();
        ParserExpects parserExpects = ParserExpects.NORMAL;
        while (stk.nextToken() != StreamTokenizer.TT_EOF) {
            String stringToWrite = null;
            if (stk.ttype == StreamTokenizer.TT_WORD) {
                switch(parserExpects) {
                    case VARIABLE:
                        parserExpects = ParserExpects.BRACKET_CLOSE;
                        variableName = stk.sval;
                        tmpBuf.append(variableName);
                        break;
                    case NORMAL:
                        stringToWrite = stk.sval;
                        break;
                    default:
                        parserExpects = ParserExpects.NORMAL;
                        stringToWrite = tmpBuf.toString() + stk.sval;
                        tmpBuf.setLength(0);
                }
            } else if (stk.ttype == '\n') {
                stringToWrite = new String(new char[] { (char) stk.ttype });
                if (parserExpects != ParserExpects.NORMAL) {
                    parserExpects = ParserExpects.NORMAL;
                    stringToWrite = tmpBuf.toString() + stringToWrite;
                    tmpBuf.setLength(0);
                }
            } else if (stk.ttype == '$') {
                if (parserExpects != ParserExpects.NORMAL) {
                    stringToWrite = tmpBuf.toString();
                    tmpBuf.setLength(0);
                }
                tmpBuf.append((char) stk.ttype);
                parserExpects = ParserExpects.BRACKET_OPEN;
            } else if (stk.ttype == '{') {
                switch(parserExpects) {
                    case NORMAL:
                        stringToWrite = new String(new char[] { (char) stk.ttype });
                        break;
                    case BRACKET_OPEN:
                        tmpBuf.append((char) stk.ttype);
                        parserExpects = ParserExpects.VARIABLE;
                        break;
                    default:
                        parserExpects = ParserExpects.NORMAL;
                        stringToWrite = tmpBuf.toString() + (char) stk.ttype;
                        tmpBuf.setLength(0);
                }
            } else if (stk.ttype == '}') {
                switch(parserExpects) {
                    case NORMAL:
                        stringToWrite = new String(new char[] { (char) stk.ttype });
                        break;
                    case BRACKET_CLOSE:
                        parserExpects = ParserExpects.NORMAL;
                        tmpBuf.append((char) stk.ttype);
                        if (variableName == null) throw new IllegalStateException("variableName is null!!!");
                        stringToWrite = variables.get(variableName);
                        if (stringToWrite == null) {
                            logger.warn("Variable " + tmpBuf.toString() + " occuring in template is unknown!");
                            stringToWrite = tmpBuf.toString();
                        }
                        tmpBuf.setLength(0);
                        break;
                    default:
                        parserExpects = ParserExpects.NORMAL;
                        stringToWrite = tmpBuf.toString() + (char) stk.ttype;
                        tmpBuf.setLength(0);
                }
            }
            if (stringToWrite != null) writer.write(stringToWrite);
        }
    }
