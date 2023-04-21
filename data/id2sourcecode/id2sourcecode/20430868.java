    public void transform(FilterParams p) {
        BufferedImage img = p.sequence.overlaidAll;
        width = img.getWidth();
        height = img.getHeight();
        arr = new int[width * height];
        img.getRGB(0, 0, width, height, arr, 0, width);
        int maxC = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int c = pixel(x, y);
                maxC = Math.max(c, maxC);
            }
        }
        int minX = width;
        int minY = height;
        int maxX = 0;
        int maxY = 0;
        int treshold = (int) (maxC * 0.2f);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int c = pixel(x, y);
                boolean inside = c > treshold;
                if (inside) {
                    minX = Math.min(x, minX);
                    minY = Math.min(y, minY);
                    maxX = Math.max(x, maxX);
                    maxY = Math.max(y, maxY);
                }
            }
        }
        Circle circle = p.sequence.circle;
        circle.center.x = (minX + maxX) / 2;
        circle.center.y = (minY + maxY) / 2;
        circle.r = (maxX - minX + maxY - minY) / 4;
    }
