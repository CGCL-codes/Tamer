    private void split(int startx, int starty, int endx, int endy) {
        if ((endx - startx) * (endy - starty) < 5) {
            return;
        }
        boolean intersect = false;
        for (int y = starty; y < endy; y++) for (int x = startx; x < endx && !intersect; x++) if (map[y][x] < 0) {
            intersect = true;
            break;
        }
        if (intersect) {
            int midx = startx + (endx - startx) / 2;
            int midy = starty + (endy - starty) / 2;
            split(startx, starty, midx, midy);
            split(midx, starty, endx, midy);
            split(startx, midy, midx, endy);
            split(midx, midy, endx, endy);
        } else {
            int we = maxNum++;
            for (int y = starty; y < endy; y++) for (int x = startx; x < endx && !intersect; x++) areas[y][x] = we;
            if (startx > 0) {
                int base = areas[starty][startx - 1];
                if (base > 0) {
                    boolean join = true;
                    for (int i = starty; i < endy; i++) if (areas[i][startx - 1] != base) {
                        join = false;
                        break;
                    }
                    if (join) replaceAreas(base, we, startx - 1, starty);
                }
            }
            if (endx < areas[0].length - 1) {
                int base = areas[starty][endx + 1];
                if (base > 0) {
                    boolean join = true;
                    for (int i = starty; i < endy; i++) if (areas[i][endx + 1] != base) {
                        join = false;
                        break;
                    }
                    if (join) replaceAreas(base, we, endx + 1, starty);
                }
            }
            if (starty > 0) {
                int base = areas[starty - 1][startx];
                if (base > 0) {
                    boolean join = true;
                    for (int i = startx; i < endx; i++) if (areas[starty - 1][i] != base) {
                        join = false;
                        break;
                    }
                    if (join) replaceAreas(base, we, startx, starty - 1);
                }
            }
            if (endy < areas.length - 1) {
                int base = areas[endy + 1][startx];
                if (base > 0) {
                    boolean join = true;
                    for (int i = startx; i < endx; i++) if (areas[endy + 1][i] != base) {
                        join = false;
                        break;
                    }
                    if (join) replaceAreas(base, we, startx, endy + 1);
                }
            }
        }
    }
