    public XorGate(final int x, final int y) {
        super();
        this.x1 = x;
        this.y1 = y;
        this.x2 = x1;
        this.y2 = y1 + 50;
        this.x3 = x1 + 50;
        this.y3 = (y1 + y2) / 2;
        this.xInputPin1 = (x1 - 5);
        this.yInputPin1 = y1 + (y2 - y1) / 3;
        this.xInputPin2 = (x1 - 5);
        this.yInputPin2 = y1 + 2 * (y2 - y1) / 3;
        this.xOutputPin = x3;
        this.yOutputPin = y3;
    }
