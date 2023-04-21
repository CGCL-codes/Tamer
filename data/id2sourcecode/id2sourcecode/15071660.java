    private char[][] herschikDobbelstenen(char[][] dicelijst, int erasedpos) {
        char[][] arrangeddices = new char[dicelijst.length - 1][boggle.data.TextReader.AANTAL_ZIJDEN];
        int i = 0;
        while (i < erasedpos) {
            arrangeddices[i] = dicelijst[i];
            i++;
        }
        while (i < arrangeddices.length) {
            arrangeddices[i] = dicelijst[i + 1];
            i++;
        }
        return arrangeddices;
    }
