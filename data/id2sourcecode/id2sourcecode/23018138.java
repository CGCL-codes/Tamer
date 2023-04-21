    public float makeConvolutionDxx(int x, int y, int filtersize) {
        int x1, x2, x3, x4, y1, y2;
        int center, lobe_size1, lobe_size2;
        center = (filtersize - 1) / 2;
        lobe_size1 = filtersize / 3;
        lobe_size2 = 5 + 4 * (filtersize - 9) / 6;
        x1 = x - center;
        x4 = x + center;
        x2 = x - center + lobe_size1;
        x3 = x + center - lobe_size1;
        y1 = y - (lobe_size2 - 1) / 2;
        y2 = y + (lobe_size2 - 1) / 2;
        return (integralImage.getIntegralSquare(x2, y1, x3, y2) * 3 - integralImage.getIntegralSquare(x1, y1, x4, y2));
    }
