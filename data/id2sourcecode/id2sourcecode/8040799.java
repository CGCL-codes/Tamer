    public boolean remove(int index) {
        if (index >= 0 && index < anzahl) {
            for (int i = index; i < anzahl - 1; i++) {
                speicher[i] = speicher[i + 1];
            }
            anzahl--;
            return true;
        } else return false;
    }
