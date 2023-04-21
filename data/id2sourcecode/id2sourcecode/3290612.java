    private static Color getRandomColor(int minColor, int maxColor) {
        Random random = new Random();
        int red = minColor + random.nextInt(maxColor - minColor);
        int green = minColor + random.nextInt(maxColor - minColor);
        int blue = minColor + random.nextInt(maxColor - minColor);
        return new Color(red, green, blue);
    }
