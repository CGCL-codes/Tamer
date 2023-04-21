    private int getMaxValue(String xChr, String yChr, long xs, long xe, long ys, long ye) {
        int value = 0;
        long interX = (xs + xe) / 2;
        long interY = (ys + ye) / 2;
        for (int i = 0; i < areas.size(); i++) {
            Area area = (Area) areas.get(i);
            if (xChr.equals(area.getXChr()) && yChr.equals(area.getYChr())) {
                if (area.getXStart() <= xs && xe <= area.getXEnd() && area.getYStart() <= ys && ye <= area.getYEnd()) {
                    if (value < area.getValue()) {
                        value = area.getValue();
                    }
                }
            }
        }
        return value;
    }
