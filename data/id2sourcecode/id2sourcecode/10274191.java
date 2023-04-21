    public BufferedImage view(Rectangle rectangle) {
        if (rectangle.equals(outOf)) {
            return view();
        }
        return bot.createScreenCapture(rectangle);
    }
