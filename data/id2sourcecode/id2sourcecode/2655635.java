    public static Color getRandColor(int fc, int bc) {
        fc = Math.min(fc, 255);
        bc = Math.min(bc, 255);
        final int r = fc + random.nextInt(bc - fc);
        final int g = fc + random.nextInt(bc - fc);
        final int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }
