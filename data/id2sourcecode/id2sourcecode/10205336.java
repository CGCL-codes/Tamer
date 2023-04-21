    public int indexOfMass(double mass) {
        if (peaks_sorted_mass.size() == 0) return -1;
        if (mass < peaks_sorted_mass.firstElement().getMass() || mass > peaks_sorted_mass.lastElement().getMass()) return -1;
        if (mass >= peaks_sorted_mass.firstElement().getMass() && mass < peaks_sorted_mass.get(1).getMass()) return 0;
        if (mass == peaks_sorted_mass.lastElement().getMass()) return peaks_sorted_mass.size() - 1;
        int index = peaks_sorted_mass.size() / 2;
        int end_index = peaks_sorted_mass.size() - 1;
        int begin_index = 0;
        while (true) {
            double current_mass = peaks_sorted_mass.get(index).getMass();
            int new_index;
            if (current_mass > mass) {
                new_index = (begin_index + index) / 2;
                end_index = index;
            } else {
                new_index = (end_index + index) / 2;
                begin_index = index;
            }
            current_mass = peaks_sorted_mass.get(new_index).getMass();
            double next_mass = peaks_sorted_mass.get(new_index + 1).getMass();
            if (current_mass <= mass && next_mass > mass) return new_index; else index = new_index;
        }
    }
