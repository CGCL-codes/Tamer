    private Color getRandColor(int min, int max) {
        Random random = new Random();
        if (min > 255) min = 255;
        if (max > 255) max = 255;
        int r = min + random.nextInt(max - min);
        int g = min + random.nextInt(max - min);
        int b = min + random.nextInt(max - min);
        return new Color(r, g, b);
    }
