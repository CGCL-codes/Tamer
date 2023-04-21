    void deleteBond(int i) {
        int newLength = bonds.length - 1;
        if (newLength == 0) {
            bonds = null;
            return;
        }
        Bond[] bondsNew = new Bond[newLength];
        int j = 0;
        for (; j < i; ++j) bondsNew[j] = bonds[j];
        for (; j < newLength; ++j) bondsNew[j] = bonds[j + 1];
        bonds = bondsNew;
    }
