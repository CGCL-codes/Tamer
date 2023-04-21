    public MyRound(int ax1, int ay1, int ax2, int ay2) {
        leftX = ax1 < ax2 ? ax1 : ax2;
        topY = ay1 > ay2 ? ay2 : ay1;
        width = Math.abs(ax1 - ax2);
        height = Math.abs(ay1 - ay2);
        d = (width + height) / 2;
        this.setFrame(leftX, topY, d, d);
    }
