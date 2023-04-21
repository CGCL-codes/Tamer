    public boolean delete(String searchName) {
        int j;
        for (j = 0; j < nElems; j++) if (a[j].getLast().equals(searchName)) break;
        if (j == nElems) return false; else {
            for (int k = j; k < nElems; k++) a[k] = a[k + 1];
            nElems--;
            return true;
        }
    }
