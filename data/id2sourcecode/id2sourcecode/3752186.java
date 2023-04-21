    public void secondClick(final int y) {
        final int midpoint = (maximum + visibleAmount) / 2;
        setPostion(y < midpoint ? minimum : maximum);
    }
