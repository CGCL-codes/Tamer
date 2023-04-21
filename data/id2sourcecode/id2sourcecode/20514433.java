    public boolean findTerm(String _term) {
        int low = -1;
        int high = numberOfLexiconEntries;
        int i;
        int compareStrings;
        if (USE_HASH) {
            int firstChar = _term.charAt(0);
            int[] boundaries = (int[]) map.get(firstChar);
            low = boundaries[0];
            high = boundaries[1];
        }
        try {
            while (high - low > 1) {
                i = (high + low) / 2;
                lexiconFile.seek((long) i * (long) lexiconEntryLength);
                lexiconFile.readFully(buffer, 0, lexiconEntryLength);
                term = new String(buffer, 0, ApplicationSetup.STRING_BYTE_LENGTH).trim();
                if ((compareStrings = _term.compareTo(term)) < 0) high = i; else if (compareStrings > 0) low = i; else {
                    seekEntry(i);
                    return true;
                }
            }
        } catch (IOException ioe) {
            logger.fatal("IOException while binary searching the lexicon: " + ioe);
        }
        if (high == numberOfLexiconEntries) return false;
        seekEntry(high);
        if (_term.compareTo(term) == 0) return true;
        return false;
    }
