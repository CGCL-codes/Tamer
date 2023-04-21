    public void setProjectionArea(Rectangle r) {
        x1 = r.x;
        x2 = x1 + r.width;
        y1 = r.y;
        y2 = y1 + r.height;
        center_x = (x1 + x2) / 2;
        center_y = (y1 + y2) / 2;
        trans_x = center_x + _2D_trans_x;
        trans_y = center_y + _2D_trans_y;
    }
