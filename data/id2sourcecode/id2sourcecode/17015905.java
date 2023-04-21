    private void checkTwoLines(String[] eols, final int maxLineIdx, final int bufSize, final boolean endWithLineBreak) throws IOException {
        for (String eol : eols) {
            String before = LINE_1 + eol + LINE_2 + (endWithLineBreak ? System.getProperty("line.separator") : "");
            log.debug(toHexString(StringUtils.getBytesFromAsciiString(before)));
            FastLineNumberReader r = getFastLineNumberReader(before, bufSize);
            StringWriter sw = new StringWriter();
            String line;
            while ((line = r.readLine()) != null) {
                log.debug("Line# " + r.getLineNumber() + ": " + line);
                sw.write(line);
                sw.write(r.readEndOfLine());
            }
            String after = sw.toString();
            assertEquals(before, after);
            assertEquals(maxLineIdx, r.getLineNumber());
        }
    }
