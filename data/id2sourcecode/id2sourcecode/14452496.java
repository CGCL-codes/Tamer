    public int getActualMaximum(int field) {
        switch(field) {
            case YEAR:
                {
                    Calendar cal = (Calendar) clone();
                    cal.setLenient(true);
                    int era = cal.get(ERA);
                    Date d = cal.getTime();
                    int lowGood = LIMITS[YEAR][1];
                    int highBad = LIMITS[YEAR][2] + 1;
                    while ((lowGood + 1) < highBad) {
                        int y = (lowGood + highBad) / 2;
                        cal.set(YEAR, y);
                        if (cal.get(YEAR) == y && cal.get(ERA) == era) {
                            lowGood = y;
                        } else {
                            highBad = y;
                            cal.setTime(d);
                        }
                    }
                    return lowGood;
                }
            default:
                return super.getActualMaximum(field);
        }
    }
