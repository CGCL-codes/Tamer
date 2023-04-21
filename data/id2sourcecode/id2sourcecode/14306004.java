    public boolean checkCoveredRelation(double inx, double iny, double inz, double threshold) {
        if (shape.equals(SHAPE_COLUMN)) {
            double r = (sizex + threshold) / 2;
            return (((inx - x) * (inx - x) + (iny - y) * (iny - y) <= r * r) && (inz > z));
        } else if (shape.equals(SHAPE_BOX)) {
            double largex = x + (threshold + sizex) / 2;
            double smallx = x - (threshold + sizex) / 2;
            double largey = y + (threshold + sizey) / 2;
            double smally = y - (threshold + sizey) / 2;
            return ((inx < largex) && (inx > smallx) && (iny < largey) && (iny > smally) && (inz > z));
        }
        return false;
    }
