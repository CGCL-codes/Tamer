    public boolean parse_word(int textloc, int wordloc, int wordlength, int parseloc, boolean flagunknown) {
        short encword[];
        long enclong;
        long dictlong;
        int dictloc;
        int first = 0;
        int last = nentries - 1;
        int middle;
        int parseentry;
        if (zm.memory_image[parseloc] == zm.memory_image[parseloc + 1]) return true;
        encword = zm.encode_word(wordloc, wordlength, 3);
        enclong = (((long) encword[0] & 0xFFFF) << 32) | (((encword[1]) & 0xFFFF) << 16) | ((encword[2] & 0xFFFF));
        middle = (last + first) / 2;
        while (true) {
            dictloc = wtable_addr + (middle * entry_length);
            dictlong = (((long) zm.memory_image[dictloc] & 0xFF) << 40) | (((long) zm.memory_image[dictloc + 1] & 0xFF) << 32) | (((long) zm.memory_image[dictloc + 2] & 0xFF) << 24) | (((long) zm.memory_image[dictloc + 3] & 0xFF) << 16) | (((long) zm.memory_image[dictloc + 4] & 0xFF) << 8) | ((long) zm.memory_image[dictloc + 5] & 0xFF);
            if (enclong < dictlong) {
                if (first == middle) break;
                last = middle - 1;
                middle = (first + middle) / 2;
            } else if (enclong > dictlong) {
                if (last == middle) break;
                first = middle + 1;
                middle = (middle + last + 1) / 2;
            } else break;
        }
        if (enclong != dictlong) {
            dictloc = 0;
        }
        if ((dictloc != 0) || flagunknown) {
            parseentry = parseloc + ((zm.memory_image[parseloc + 1] & 0xFF) * 4) + 2;
            zm.memory_image[parseentry] = (byte) ((dictloc & 0xFF00) >> 8);
            zm.memory_image[parseentry + 1] = (byte) (dictloc & 0xFF);
            zm.memory_image[parseentry + 2] = (byte) wordlength;
            zm.memory_image[parseentry + 3] = (byte) (wordloc - textloc + 2);
        }
        zm.memory_image[parseloc + 1]++;
        if (zm.memory_image[parseloc] == zm.memory_image[parseloc + 1]) return true;
        return false;
    }
