    public void usunElement(int nr) {
        boolean zmien = false;
        for (int i = 0; i < inArt; i++) {
            if (i == nr) {
                zmien = true;
            }
            if (zmien) {
                art[i] = art[i + 1];
            }
        }
        if (zmien) {
            inArt--;
        }
    }
