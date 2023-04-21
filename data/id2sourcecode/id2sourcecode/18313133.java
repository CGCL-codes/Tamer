    public int getLineForOffset(int offset) {
        int high = getLineCount(), low = -1, guess;
        while (high - low > 1) {
            guess = (high + low) / 2;
            if (getLineStart(guess) > offset) high = guess; else low = guess;
        }
        if (low < 0) return 0; else return low;
    }
