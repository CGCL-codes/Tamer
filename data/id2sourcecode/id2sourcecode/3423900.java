    public void secondClick(final int y) {
        int midpoint = (maximum + visibleAmount) / 2;
        setPostion(y < midpoint ? minimum : maximum);
    }
