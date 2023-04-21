    protected void setPosition(int newPos) {
        pos = newPos;
        long newDelta;
        if (pos > 0) {
            newDelta = pos * 1000 - (estimatedPos + 500);
            if (Math.abs(newDelta) > 4000) {
                estimatedPos = pos * 1000;
                delta = 0;
            } else {
                newDelta /= 10;
                delta = (delta + newDelta) / 2;
            }
        }
        firePositionUpdated();
    }
