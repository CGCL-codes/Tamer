    public boolean add(JHumSymbol s, int diff) {
        if (getMaxX() <= s.x) return addOrSet(size(), s, diff);
        int inf = 0;
        int sup = size() - 1;
        while (Math.abs(inf - sup) > 1) {
            int center = (inf + sup) / 2;
            if (s.x >= get(center).x) inf = center; else sup = center;
        }
        if (get(inf).x >= s.x) addOrSet(inf, s, diff); else addOrSet(inf + 1, s, diff);
        return true;
    }
