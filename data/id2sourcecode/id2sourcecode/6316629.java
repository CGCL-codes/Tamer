    public Point[] getSelectionCrossPoint() {
        if (hasSelection() && mode == MODE_CROSS) {
            Point[] pts = new Point[4];
            Point p1 = new Point();
            Point p2 = new Point();
            Point p3 = new Point();
            Point p4 = new Point();
            pts[0] = p1;
            pts[1] = p2;
            pts[2] = p3;
            pts[3] = p4;
            switch(getHorizontalPosition()) {
                case HORIZONTAL_TOP:
                    p1.x = x1;
                    p2.x = x2;
                    p1.y = y1;
                    p2.y = y1;
                    break;
                case HORIZONTAL_CENTER:
                    p1.x = x1;
                    p2.x = x2;
                    p1.y = (y1 + y2) / 2;
                    p2.y = (y1 + y2) / 2;
                    break;
                case HORIZONTAL_BOTTOM:
                    p1.x = x1;
                    p2.x = x2;
                    p1.y = y2;
                    p2.y = y2;
                    break;
            }
            switch(getVerticalPosition()) {
                case VERTICAL_LEFT:
                    p3.x = x1;
                    p4.x = x1;
                    p3.y = y1;
                    p4.y = y2;
                    break;
                case VERTICAL_CENTER:
                    p3.x = (x1 + x2) / 2;
                    p4.x = (x1 + x2) / 2;
                    p3.y = y1;
                    p4.y = y2;
                    break;
                case VERTICAL_RIGHT:
                    p3.x = x2;
                    p4.x = x2;
                    p3.y = y1;
                    p4.y = y2;
                    break;
            }
            return pts;
        } else {
            return null;
        }
    }
