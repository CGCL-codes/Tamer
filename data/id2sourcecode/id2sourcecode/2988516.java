    private int getClosestTab(int x, int y) {
        int min = 0;
        int tabCount = Math.min(rects.length, tabPane.getTabCount());
        int max = tabCount;
        int tabPlacement = tabPane.getTabPlacement();
        boolean useX = (tabPlacement == TOP || tabPlacement == BOTTOM);
        int want = (useX) ? x : y;
        while (min != max) {
            int current = (max + min) / 2;
            int minLoc;
            int maxLoc;
            if (useX) {
                minLoc = rects[current].x;
                maxLoc = minLoc + rects[current].width;
            } else {
                minLoc = rects[current].y;
                maxLoc = minLoc + rects[current].height;
            }
            if (want < minLoc) {
                max = current;
                if (min == max) {
                    return Math.max(0, current - 1);
                }
            } else if (want >= maxLoc) {
                min = current;
                if (max - min <= 1) {
                    return Math.max(current + 1, tabCount - 1);
                }
            } else {
                return current;
            }
        }
        return min;
    }
