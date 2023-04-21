    protected void layout(float radLo, float radHi, float thLo, float thHi) {
        if (radHi - radLo < minRadius) {
            radHi = radLo + minRadius;
        }
        this.rLo = radLo;
        this.rHi = radHi;
        this.tLo = thLo;
        this.tHi = thHi;
        this.radius = radHi;
        this.layoutText();
        this.createShapes();
        float tMid = (tHi + tLo) / 2;
        float dTheta = tHi - tLo;
        float minTheta = PApplet.QUARTER_PI * .6f * items.size();
        float maxTheta = Math.min(PApplet.HALF_PI * 1.5f, dTheta);
        dTheta = PApplet.constrain(dTheta, minTheta, maxTheta);
        layoutSubItems(rLo, rHi, tMid - dTheta / 2, tMid + dTheta / 2);
    }
