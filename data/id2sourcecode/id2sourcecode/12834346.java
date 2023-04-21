    private static Color getRandColor(int cc, int bb) {
        Random random = new Random();
        if (cc > 255) cc = 255;
        if (bb > 255) bb = 255;
        int r = cc + random.nextInt(bb - cc);
        int g = cc + random.nextInt(bb - cc);
        int b = cc + random.nextInt(bb - cc);
        return new Color(r, g, b);
    }
