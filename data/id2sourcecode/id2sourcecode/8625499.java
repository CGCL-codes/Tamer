    protected void doParse(String path) throws IOException, OBOParseException {
        setProgressString("Reading " + path);
        String currentStanza = null;
        BufferedReader reader = null;
        if (reader == null) {
            BufferedInputStream is = new BufferedInputStream(IOUtil.getProgressableStream(path));
            reader = new BufferedReader(new InputStreamReader(is, OBOConstants.DEFAULT_CHARACTER_ENCODING));
        }
        URL url = IOUtil.getURL(path);
        totalSize += url.openConnection().getContentLength();
        for (linenum = 1; (line = reader.readLine()) != null; linenum++) {
            if (halt) throw new OBOParseException("Operation cancelled by user", getCurrentPath(), null, -1);
            bytesRead += line.length();
            if (line.length() == 0) continue;
            while (line.charAt(line.length() - 1) == '\\' && line.charAt(line.length() - 2) != '\\') {
                String str = reader.readLine();
                linenum++;
                if (str == null) throw new OBOParseException("Unexpected end of file", getCurrentPath(), line, linenum);
                line = line.substring(0, line.length() - 1) + str;
            }
            if (line.charAt(0) == '!') {
                parser.readBangComment(line.substring(1));
            } else if (line.charAt(0) == '[') {
                if (line.charAt(line.length() - 1) != ']') throw new OBOParseException("Unclosed stanza \"" + line + "\"", getCurrentPath(), line, linenum);
                String stanzaname = line.substring(1, line.length() - 1);
                if (stanzaname.length() < 1) throw new OBOParseException("Empty stanza", getCurrentPath(), line, linenum);
                currentStanza = stanzaname;
                parser.startStanza(stanzaname);
                setReadIDForCurrentBlock(false);
            } else {
                while (line.length() > 0 && Character.isWhitespace(line.charAt(0))) {
                    line = line.substring(1);
                }
                if (line.length() <= 1) {
                    continue;
                }
                SOPair pair;
                try {
                    pair = unescape(line, ':', 0, true);
                } catch (OBOParseException ex) {
                    translateAndThrow(ex, line, linenum, 0);
                    break;
                }
                String name = pair.str;
                int lineEnd = findUnescaped(line, '!', 0, line.length(), true);
                if (lineEnd == -1) lineEnd = line.length();
                NestedValue nv = null;
                int trailingStartIndex = -1;
                int trailingEndIndex = -1;
                for (int i = lineEnd - 1; i >= 0; i--) {
                    if (Character.isWhitespace(line.charAt(i))) {
                    } else if (line.charAt(i) == '}') {
                        if (i >= 1 && line.charAt(i - 1) == '\\') continue;
                        trailingEndIndex = i;
                        break;
                    } else break;
                }
                if (trailingEndIndex != -1) {
                    for (int i = trailingEndIndex - 1; i >= 0; i--) {
                        if (line.charAt(i) == '{') {
                            if (i >= 1 && line.charAt(i - 1) == '\\') continue;
                            trailingStartIndex = i + 1;
                        }
                    }
                }
                int valueStopIndex;
                if (trailingStartIndex == -1 && trailingEndIndex != -1) throw new OBOParseException("Unterminated " + "trailing modifier.", getCurrentPath(), line, linenum, 0); else if (trailingStartIndex != -1) {
                    valueStopIndex = trailingStartIndex - 1;
                    String trailing = line.substring(trailingStartIndex, trailingEndIndex).trim();
                    nv = new NestedValueImpl();
                    getNestedValue(nv, trailing, 0);
                } else valueStopIndex = lineEnd;
                String value = null;
                try {
                    value = line.substring(pair.index + 1, valueStopIndex);
                } catch (Throwable t) {
                    throw new OBOParseException("Invalid string index", getCurrentPath(), line, linenum);
                }
                if (value.length() == 0) throw new OBOParseException("Tag found with no value", getCurrentPath(), line, linenum);
                if (parser instanceof OBOParser) {
                    try {
                        parseTag(currentStanza, line, linenum, pair.index + 1, name, value, nv);
                    } catch (OBOParseException ex) {
                        ex.printStackTrace();
                        translateAndThrow(ex, line, linenum, pair.index + 1);
                    }
                } else {
                    try {
                        parser.readTagValue(name, value, nv, false);
                    } catch (OBOParseException ex) {
                        translateAndThrow(ex, line, linenum, pair.index + 1);
                    }
                }
            }
            int percentVal = 100 * bytesRead / totalSize;
            setProgressValue(percentVal);
        }
    }
