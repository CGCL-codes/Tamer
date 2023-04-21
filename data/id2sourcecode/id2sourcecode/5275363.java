    public boolean ausmustern(int index) {
        boolean ausgemustert = false;
        if (index >= 0 && index < anzahl) {
            for (int i = index; i < anzahl - 1; i++) buecher[i] = buecher[i + 1];
            anzahl--;
            ausgemustert = true;
        }
        return ausgemustert;
    }
