    private void resetMembers() {
        double minIncrement = incrementGenerator.getMin();
        double maxIncrement = incrementGenerator.getMax();
        if (minIncrement < 0 && maxIncrement <= 0) next = max; else if (minIncrement >= 0 && maxIncrement > 0) next = min; else next = (min + max) / 2;
    }
