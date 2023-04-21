    public boolean parse_word(int textloc, int wordloc, int wordlength, int parseloc) {
        short encword[];
        int encint;
        int dictint;
        int dictloc;
        int first = 0;
        int last = nentries - 1;
        int middle;
        int parseentry;
        if (zm.memory_image[parseloc] == zm.memory_image[parseloc + 1]) return true;
        encword = zm.encode_word(wordloc, wordlength, 2);
        encint = ((encword[0] & 0xFFFF) << 16) | ((encword[1]) & 0xFFFF);
        middle = (last + first) / 2;
        while (true) {
            dictloc = wtable_addr + (middle * entry_length);
            dictint = ((zm.memory_image[dictloc] & 0xFF) << 24) | ((zm.memory_image[dictloc + 1] & 0xFF) << 16) | ((zm.memory_image[dictloc + 2] & 0xFF) << 8) | (zm.memory_image[dictloc + 3] & 0xFF);
            if (encint < dictint) {
                if (first == middle) break;
                last = middle - 1;
                middle = (first + middle) / 2;
            } else if (encint > dictint) {
                if (last == middle) break;
                first = middle + 1;
                middle = (middle + last + 1) / 2;
            } else break;
        }
        if (encint != dictint) {
            dictloc = 0;
        }
        parseentry = parseloc + ((zm.memory_image[parseloc + 1] & 0xFF) * 4) + 2;
        zm.memory_image[parseentry] = (byte) ((dictloc & 0xFF00) >> 8);
        zm.memory_image[parseentry + 1] = (byte) (dictloc & 0xFF);
        zm.memory_image[parseentry + 2] = (byte) wordlength;
        zm.memory_image[parseentry + 3] = (byte) (wordloc - textloc + 1);
        zm.memory_image[parseloc + 1]++;
        if (zm.memory_image[parseloc] == zm.memory_image[parseloc + 1]) return true;
        return false;
    }
