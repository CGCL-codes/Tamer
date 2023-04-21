    public float makeConvolutionDyy(int x, int y, int filtersize) {
        int x1, x2, y1, y2, y3, y4;
        int center, lobeSize_1, lobeSize_2;
        center = (filtersize - 1) / 2;
        lobeSize_1 = filtersize / 3;
        lobeSize_2 = 5 + 4 * (filtersize - 9) / 6;
        y1 = y - center;
        y4 = y + center;
        y2 = y - center + lobeSize_1;
        y3 = y + center - lobeSize_1;
        x1 = x - (lobeSize_2 - 1) / 2;
        x2 = x + (lobeSize_2 - 1) / 2;
        return (integralImage.getIntegralSquare(x1, y2, x2, y3) * 3 - integralImage.getIntegralSquare(x1, y1, x2, y4));
    }
