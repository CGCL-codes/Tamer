    public Selection point2Offset(Point p, Selection o) {
        if (p.y < yInset) {
            o.caret = 0;
            o.clickAfter = true;
            return o;
        }
        int line = (p.y - yInset) / lineHeight;
        if (line >= lineCount) {
            o.caret = contents.length();
            o.clickAfter = false;
            return o;
        }
        int target = p.x - xInset;
        if (target <= 0) {
            o.caret = lineStarts[line];
            o.clickAfter = true;
            return o;
        }
        int lowGuess = lineStarts[line];
        int lowWidth = 0;
        int highGuess = lineStarts[line + 1];
        int highWidth = fm.stringWidth(contents.substring(lineStarts[line], highGuess));
        if (target >= highWidth) {
            o.caret = lineStarts[line + 1];
            o.clickAfter = false;
            return o;
        }
        while (lowGuess < highGuess - 1) {
            int guess = (lowGuess + highGuess) / 2;
            int width = fm.stringWidth(contents.substring(lineStarts[line], guess));
            if (width <= target) {
                lowGuess = guess;
                lowWidth = width;
                if (width == target) break;
            } else {
                highGuess = guess;
                highWidth = width;
            }
        }
        int highBound = charBreaker.following(lowGuess);
        int lowBound = charBreaker.previous();
        if (lowBound != lowGuess) lowWidth = fm.stringWidth(contents.substring(lineStarts[line], lowBound));
        if (highBound != highGuess) highWidth = fm.stringWidth(contents.substring(lineStarts[line], highBound));
        if (target - lowWidth < highWidth - target) {
            o.caret = lowBound;
            o.clickAfter = true;
        } else {
            o.caret = highBound;
            o.clickAfter = false;
        }
        return o;
    }
