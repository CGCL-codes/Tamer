    public static char[] separateStringBeginning(char[] chararr, char separation, int charcount) {
        if (chararr == null || charcount < 1) {
            return null;
        }
        if (charcount > chararr.length) {
            charcount = chararr.length;
        }
        int newlen = charcount * 2 - 1;
        char newstr[] = new char[newlen];
        int pos;
        newstr[0] = chararr[0];
        for (int i = 1; i < newlen; i += 2) {
            pos = (i + 1) / 2;
            newstr[i] = separation;
            newstr[i + 1] = chararr[pos];
        }
        return newstr;
    }
