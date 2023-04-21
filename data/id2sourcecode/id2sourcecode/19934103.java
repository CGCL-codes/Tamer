    private static int packedsize(int integer, int decimal) {
        int size = integer + decimal;
        if (size % 2 == 0) size = (size / 2) + 1; else size = (size + 1) / 2;
        return size;
    }
