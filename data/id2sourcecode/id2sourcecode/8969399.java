    public static char[] separateStringEx(char[] chararr, char separation) {
        if (chararr == null) {
            return null;
        }
        int newlen = chararr.length * 2 - 1;
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
