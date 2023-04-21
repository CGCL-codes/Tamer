    private static int getFit(TextPaint paint, TextPaint workPaint, CharSequence text, int start, int end, float wid) {
        int high = end + 1, low = start - 1, guess;
        while (high - low > 1) {
            guess = (high + low) / 2;
            if (measureText(paint, workPaint, text, start, guess, null, true, null) > wid) high = guess; else low = guess;
        }
        if (low < start) return start; else return low;
    }
