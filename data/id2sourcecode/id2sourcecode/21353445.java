    public void calibrate() {
        colorLightValue = read("finish color");
        blackLightValue = read("black");
        whiteLightValue = read("white");
        blackWhiteThreshold = (blackLightValue + whiteLightValue) / 2;
    }
