    public void secondClick(int y) {
        int midpoint = (maximum + visibleAmount) / 2;
        setPostion(y < midpoint ? minimum : maximum);
    }
