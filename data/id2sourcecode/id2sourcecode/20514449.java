    public LexiconEntry getLexiconEntry(String _term) {
        int low = -1;
        int high = numberOfLexiconEntries;
        int i;
        int compareStrings;
        String term;
        byte[] buffer = new byte[lexiconEntryLength + 9];
        if (USE_HASH) {
            int firstChar = _term.charAt(0);
            int[] boundaries = (int[]) map.get(firstChar);
            if (boundaries != null) {
                low = boundaries[0];
                high = boundaries[1];
            }
        }
        try {
            while (high - low > 1) {
                i = (high + low) / 2;
                if (i == 0) {
                    lexiconFile.seek(0);
                    lexiconFile.readFully(buffer, 0, lexiconEntryLength);
                    term = new String(buffer, 0, ApplicationSetup.STRING_BYTE_LENGTH).trim();
                } else {
                    lexiconFile.seek((long) i * (long) (lexiconEntryLength) - 9L);
                    lexiconFile.readFully(buffer, 0, lexiconEntryLength + 9);
                    term = new String(buffer, 9, ApplicationSetup.STRING_BYTE_LENGTH).trim();
                }
                if ((compareStrings = _term.compareTo(term)) < 0) high = i; else if (compareStrings > 0) low = i; else {
                    return getLexiconEntryFromBuffer(buffer, term, i);
                }
            }
            if (high == numberOfLexiconEntries) return null;
            if (high == 0) {
                lexiconFile.seek(0);
                lexiconFile.readFully(buffer, 0, lexiconEntryLength);
                term = new String(buffer, 0, ApplicationSetup.STRING_BYTE_LENGTH).trim();
            } else {
                lexiconFile.seek((long) high * (long) (lexiconEntryLength) - 9L);
                lexiconFile.readFully(buffer, 0, lexiconEntryLength + 9);
                term = new String(buffer, 9, ApplicationSetup.STRING_BYTE_LENGTH).trim();
            }
            if (_term.compareTo(term) == 0) {
                return getLexiconEntryFromBuffer(buffer, term, high);
            }
        } catch (IOException ioe) {
            logger.fatal("IOException while binary searching the lexicon: " + ioe);
        }
        return null;
    }
