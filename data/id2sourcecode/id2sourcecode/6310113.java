    private Color getRandColor(int fc, int bc) {
        int fc_a = fc;
        int bc_a = bc;
        Random random = new Random();
        if (fc > 255) fc_a = 255;
        if (bc > 255) bc_a = 255;
        int r = fc + random.nextInt(bc_a - fc_a);
        int g = fc + random.nextInt(bc_a - fc_a);
        int b = fc + random.nextInt(bc_a - fc_a);
        return new Color(r, g, b);
    }
