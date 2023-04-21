    private Token getToken(char character) {
        Token token = new Token();
        final String letter = Character.toString(character);
        token.source = letter;
        int offset = -1;
        int cmp;
        if (character < 256) {
            token.type = Token.LATIN;
            token.target = letter;
            return token;
        } else if (character < FIRST_UNIHAN) {
            token.type = Token.UNKNOWN;
            token.target = letter;
            return token;
        } else {
            cmp = COLLATOR.compare(letter, FIRST_PINYIN_UNIHAN);
            if (cmp < 0) {
                token.type = Token.UNKNOWN;
                token.target = letter;
                return token;
            } else if (cmp == 0) {
                token.type = Token.PINYIN;
                offset = 0;
            } else {
                cmp = COLLATOR.compare(letter, LAST_PINYIN_UNIHAN);
                if (cmp > 0) {
                    token.type = Token.UNKNOWN;
                    token.target = letter;
                    return token;
                } else if (cmp == 0) {
                    token.type = Token.PINYIN;
                    offset = UNIHANS.length - 1;
                }
            }
        }
        token.type = Token.PINYIN;
        if (offset < 0) {
            int begin = 0;
            int end = UNIHANS.length - 1;
            while (begin <= end) {
                offset = (begin + end) / 2;
                final String unihan = Character.toString(UNIHANS[offset]);
                cmp = COLLATOR.compare(letter, unihan);
                if (cmp == 0) {
                    break;
                } else if (cmp > 0) {
                    begin = offset + 1;
                } else {
                    end = offset - 1;
                }
            }
        }
        if (cmp < 0) {
            offset--;
        }
        StringBuilder pinyin = new StringBuilder();
        for (int j = 0; j < PINYINS[offset].length && PINYINS[offset][j] != 0; j++) {
            pinyin.append((char) PINYINS[offset][j]);
        }
        token.target = pinyin.toString();
        return token;
    }
