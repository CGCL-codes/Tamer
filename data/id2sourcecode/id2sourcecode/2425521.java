    public static void interpolation(Point[] spotList) {
        int i;
        int count = 0;
        for (i = 0; i < SLOT; ++i) bucket[i] = 0;
        for (i = 0; i < 108; ++i) {
            if (spotList[i].x == -1) continue;
            bucket[(int) (spotList[i].x / (960 / SLOT))]++;
            count++;
        }
        for (i = 0; i < SLOT && bucket[i] <= Math.ceil(count / SLOT); ++i) ;
        lower = (960 / SLOT) * (i > 0 ? i - 1 : 0);
        for (i = SLOT - 1; i >= 0 && bucket[i] <= Math.ceil(count / SLOT); --i) ;
        upper = (960 / SLOT) * (i < SLOT - 1 ? i + 2 : SLOT);
        Log.d("interpola", String.format("lower: %f upper:%f", lower, upper));
        int lastIndex = -1, thisIndex = 0, lastMid = -1, thisMid = 0;
        for (thisIndex = 0; thisIndex < 108; ++thisIndex) {
            if (isBad(spotList[thisIndex])) {
                spotList[thisIndex].x = -1;
                spotList[thisIndex].y = -1;
                if (lastMid != -1) {
                    thisMid = (thisIndex + lastIndex) / 2;
                    refine(spotList, lastMid, thisMid);
                    lastMid = -1;
                    lastIndex = -1;
                }
                continue;
            }
            if (lastIndex == -1) {
                lastIndex = thisIndex;
                continue;
            }
            if (spotList[thisIndex].x == spotList[lastIndex].x) continue;
            if (lastMid == -1) {
                lastMid = (thisIndex + lastIndex) / 2;
                lastIndex = thisIndex;
                continue;
            }
            thisMid = (thisIndex + lastIndex) / 2;
            refine(spotList, lastMid, thisMid);
            lastIndex = thisIndex;
            lastMid = thisMid;
        }
        if (lastMid != -1) {
            thisMid = (108 + lastIndex) / 2;
            refine(spotList, lastMid, thisMid);
        }
        for (i = 0; i < 108; ++i) Log.d("inter", String.format("x: %f, y: %f", spotList[i].x, spotList[i].y));
    }
