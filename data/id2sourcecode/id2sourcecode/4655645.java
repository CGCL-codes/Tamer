    public static final int align(int iAlign, int origin, int size, int itemsize) {
        int iOrigin = origin;
        switch(iAlign) {
            case LEFT:
            case TOP:
                break;
            case RIGHT:
            case BOTTOM:
                iOrigin = origin + (size - itemsize);
                break;
            case HCENTER:
            case VCENTER:
                iOrigin = origin + (size - itemsize) / 2;
                break;
        }
        return iOrigin;
    }
