    public int getOffsetForHorizontal(int line, float horiz) {
        int max = getLineEnd(line) - 1;
        int min = getLineStart(line);
        Directions dirs = getLineDirections(line);
        if (line == getLineCount() - 1) max++;
        int best = min;
        float bestdist = Math.abs(getPrimaryHorizontal(best) - horiz);
        int here = min;
        for (int i = 0; i < dirs.mDirections.length; i++) {
            int there = here + dirs.mDirections[i];
            int swap = ((i & 1) == 0) ? 1 : -1;
            if (there > max) there = max;
            int high = there - 1 + 1, low = here + 1 - 1, guess;
            while (high - low > 1) {
                guess = (high + low) / 2;
                int adguess = getOffsetAtStartOf(guess);
                if (getPrimaryHorizontal(adguess) * swap >= horiz * swap) high = guess; else low = guess;
            }
            if (low < here + 1) low = here + 1;
            if (low < there) {
                low = getOffsetAtStartOf(low);
                float dist = Math.abs(getPrimaryHorizontal(low) - horiz);
                int aft = TextUtils.getOffsetAfter(mText, low);
                if (aft < there) {
                    float other = Math.abs(getPrimaryHorizontal(aft) - horiz);
                    if (other < dist) {
                        dist = other;
                        low = aft;
                    }
                }
                if (dist < bestdist) {
                    bestdist = dist;
                    best = low;
                }
            }
            float dist = Math.abs(getPrimaryHorizontal(here) - horiz);
            if (dist < bestdist) {
                bestdist = dist;
                best = here;
            }
            here = there;
        }
        float dist = Math.abs(getPrimaryHorizontal(max) - horiz);
        if (dist < bestdist) {
            bestdist = dist;
            best = max;
        }
        return best;
    }
