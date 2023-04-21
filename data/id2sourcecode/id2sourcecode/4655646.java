    public static final Point align(int iAlign, int width, int height, int itemLength, int itemHeight) {
        int xAdjust = 0;
        int yAdjust = 0;
        switch(iAlign & HORIZONTAL) {
            case LEFT:
                break;
            case RIGHT:
                xAdjust = width - itemLength;
                break;
            case HCENTER:
                xAdjust = (width - itemLength) / 2;
                break;
        }
        switch(iAlign & VERTICAL) {
            case TOP:
                yAdjust = itemHeight;
                break;
            case BOTTOM:
                yAdjust = height;
                break;
            case VCENTER:
                yAdjust = itemHeight + (height - itemHeight) / 2;
                break;
        }
        return new Point(xAdjust, yAdjust);
    }
