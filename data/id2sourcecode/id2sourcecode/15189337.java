    public int[] getIndexOfMass(double mass) {
        getIndexTableAscendingMass();
        if (size <= 2) return new int[] { -1, -1 };
        int indx0 = (int) indextable_ascending_mass[0];
        int indx1 = (int) indextable_ascending_mass[1];
        int indxn = (int) indextable_ascending_mass[size - 1];
        if (mass < masses[indx0]) return new int[] { -1, -1 };
        if (mass >= masses[indx0] && mass <= masses[indx1]) return new int[] { indx0, 0 };
        if (mass >= masses[indxn]) return new int[] { indxn, size - 1 };
        int index = size / 2;
        int index_bgn = 0;
        int index_end = size - 1;
        while (true) {
            int indx = (int) indextable_ascending_mass[index];
            int indxpp = (int) indextable_ascending_mass[index + 1];
            if (mass >= masses[indx] && mass <= masses[indxpp]) return new int[] { indx, index };
            int index_new;
            if (masses[indx] > mass) {
                index_new = (index_bgn + index) / 2;
                index_end = index;
            } else {
                index_new = (index_end + index) / 2;
                index_bgn = index;
            }
            index = index_new;
        }
    }
