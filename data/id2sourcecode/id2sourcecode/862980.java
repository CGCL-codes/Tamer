    public boolean usunElementZnak(String key) {
        boolean zmien = false;
        String line1 = makeLine();
        podzielNaWszystkieNawiasy(line1);
        for (int i = 0; i < inArt; i++) {
            if (art[i].equalsIgnoreCase(key)) {
                zmien = true;
            }
            if (zmien) {
                art[i] = art[i + 1];
            }
        }
        if (zmien) {
            inArt--;
        }
        return zmien;
    }
