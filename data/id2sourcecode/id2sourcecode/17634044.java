    public void calibrate() {
        blackLightValue = read("black");
        whiteLightValue = read("white");
        blackWhiteThreshold = (blackLightValue + whiteLightValue) / 2;
    }
