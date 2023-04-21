    public String doParse(IOBOParserProgress progress) throws IOException, OBOParserException {
        int currentTerm = 0;
        long millis = 0;
        BufferedReader reader;
        FileInputStream fis = new FileInputStream(filename);
        try {
            reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(fis)));
        } catch (IOException exp) {
            fis = new FileInputStream(filename);
            reader = new BufferedReader(new InputStreamReader(fis));
        }
        FileChannel fc = fis.getChannel();
        if (progress != null) progress.init((int) fc.size());
        for (linenum = 1; (line = reader.readLine()) != null; linenum++) {
            if (progress != null) {
                long newMillis = System.currentTimeMillis();
                if (newMillis - millis > 250) {
                    progress.update((int) fc.position(), currentTerm);
                    millis = newMillis;
                }
            }
            bytesRead += line.length();
            line = stripSpecialCharacters(line);
            if (line.length() == 0) continue;
            while (line.charAt(line.length() - 1) == '\\' && line.charAt(line.length() - 2) != '\\') {
                String str = reader.readLine();
                linenum++;
                if (str == null) throw new OBOParserException("Unexpected end of file", line, linenum);
                line = line.substring(0, line.length() - 1) + str;
            }
            if (line.charAt(0) == '!') continue;
            if (line.charAt(0) == '[') {
                enterNewTerm();
                currentTerm++;
                if (line.charAt(line.length() - 1) != ']') throw new OBOParserException("Unclosed stanza \"" + line + "\"", line, linenum);
                String stanzaname = line.substring(1, line.length() - 1);
                if (stanzaname.length() < 1) throw new OBOParserException("Empty stanza", line, linenum);
                if (stanzaname.equalsIgnoreCase("term")) currentStanza = Stanza.TERM; else if (stanzaname.equalsIgnoreCase("typedef")) currentStanza = Stanza.TYPEDEF; else throw new IllegalArgumentException("Unknown stanza type: \"" + stanzaname + "\" at line " + linenum);
            } else {
                try {
                    SOPair pair;
                    try {
                        pair = unescape(line, ':', 0, true);
                    } catch (OBOParserException ex) {
                        ex.linenum = linenum;
                        throw ex;
                    }
                    String name = pair.str;
                    int lineEnd = findUnescaped(line, '!', 0, line.length());
                    if (lineEnd == -1) lineEnd = line.length();
                    int trailingStartIndex = -1;
                    for (int i = lineEnd - 1; i >= 0; i--) {
                        if (Character.isWhitespace(line.charAt(i))) continue; else break;
                    }
                    int stopIndex = trailingStartIndex;
                    if (stopIndex == -1) stopIndex = lineEnd;
                    String value = line.substring(pair.index + 1, stopIndex);
                    if (value.length() == 0) throw new OBOParserException("Tag found with no value", line, linenum);
                    if (currentStanza == null) readHeaderValue(name, value); else readTagValue(name, value);
                } catch (IllegalArgumentException iae) {
                    logger.severe("Unable to parse line at " + linenum + " " + line);
                    throw iae;
                }
            }
        }
        enterNewTerm();
        if (progress != null) progress.update((int) fc.size(), currentTerm);
        reader.close();
        logger.info("Got " + terms.size() + " terms and " + numberOfRelations + " relations");
        return this.getParseDiagnostics();
    }
