    @Override
    public RgbGrid view(Rectangle rectangle) {
        return new ArrayRgbGrid(bot.createScreenCapture(rectangle), "Screen");
    }
