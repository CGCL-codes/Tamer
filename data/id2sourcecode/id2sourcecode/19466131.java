    private static Color getRandColor(int fc, int bc) {
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + RandomUtil.nextInt(bc - fc);
        int g = fc + RandomUtil.nextInt(bc - fc);
        int b = fc + RandomUtil.nextInt(bc - fc);
        return new Color(r, g, b);
    }
