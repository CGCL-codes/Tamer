    private void TableComputeChildPosition(TableLocRec rec, Component comp) {
        int cell_w, cell_h;
        int cell_x, x;
        int cell_y, y;
        int width, prefer, height;
        int i, pad;
        pad = col_spacing;
        cell_w = -pad;
        for (i = 0; i < rec.col_span; i++) cell_w += cols.elementAt(rec.col + i).value + pad;
        prefer = rec.preferredWidth();
        if (rec.options.W && cell_w > prefer) {
            width = prefer;
        } else {
            width = cell_w;
        }
        if (width <= 0) width = 1;
        pad = row_spacing;
        cell_h = -pad;
        for (i = 0; i < rec.row_span; i++) cell_h += rows.elementAt(rec.row + i).value + pad;
        prefer = rec.preferredHeight();
        if (rec.options.H && cell_h > prefer) {
            height = prefer;
        } else {
            height = cell_h;
        }
        if (height <= 0) height = 1;
        cell_x = cols.elementAt(rec.col).offset;
        if (rec.options.l) x = cell_x; else if (rec.options.r) x = cell_x + cell_w - width; else x = cell_x + (cell_w - width) / 2;
        cell_y = rows.elementAt(rec.row).offset;
        if (rec.options.t) y = cell_y; else if (rec.options.b) y = cell_y + cell_h - height; else y = cell_y + (cell_h - height) / 2;
        comp.setSize(width, height);
        Point p = comp.getLocation();
        if (x != p.x || y != p.y) {
            comp.setLocation(x, y);
        }
    }
