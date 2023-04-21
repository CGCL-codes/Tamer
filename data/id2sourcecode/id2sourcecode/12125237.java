    protected Point getAlignedPosition(int componentWidth, int componentHeight, int left, int top, int width, int height, int alignment) {
        Point result = new Point();
        int vertical = alignment & VERTICAL;
        switch(vertical) {
            case VCENTER:
                result.y = top + (height - componentHeight) / 2;
                break;
            case TOP:
                result.y = top;
                break;
            case BOTTOM:
                result.y = (top + height) - componentHeight;
                break;
        }
        int horizontal = alignment & HORIZONTAL;
        switch(horizontal) {
            case HCENTER:
                result.x = left + (width - componentWidth) / 2;
                break;
            case LEFT:
                result.x = left;
                break;
            case RIGHT:
                result.x = (left + width) - componentWidth;
                break;
        }
        return result;
    }
