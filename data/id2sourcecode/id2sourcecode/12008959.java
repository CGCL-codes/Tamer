    public NotGate(final int x, final int y) {
        super();
        this.x1 = x;
        this.y1 = y;
        this.x2 = x1;
        this.y2 = y1 + 30;
        this.x3 = x1 + 50;
        this.y3 = (y1 + y2) / 2;
        this.xInputPin = x1;
        this.yInputPin = y1 + (y2 - y1) / 2;
        this.xOutputPin = x3 + 10;
        this.yOutputPin = y3;
    }
