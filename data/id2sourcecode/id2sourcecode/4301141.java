    private static Color getRandColor(int color1, int color2) {
        Random random = new Random();
        if (color1 > 255) color1 = 255;
        if (color2 > 255) color2 = 255;
        int r = color1 + random.nextInt(color2 - color1);
        int g = color1 + random.nextInt(color2 - color1);
        int b = color1 + random.nextInt(color2 - color1);
        return new Color(r, g, b);
    }
