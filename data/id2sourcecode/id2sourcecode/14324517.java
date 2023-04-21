    private MapRects calcMap(final MapRect r, final MapList ml, final int ns, final int ne, final double sumweight) {
        if (ne - ns == 0) {
            final MapRects rects = new MapRects();
            rects.add(new MapRect(r, ml.get(ns)));
            return rects;
        }
        final MapRects rects = new MapRects();
        double weight = 0;
        final int ni = ns + (ne - ns) / 2;
        for (int i = ns; i <= ni; i++) {
            weight += ml.weight[i];
        }
        int xx = r.x;
        int yy = r.y;
        int ww = !(r.w > r.h) ? r.w : (int) (r.w * 1 / sumweight * weight);
        int hh = r.w > r.h ? r.h : (int) (r.h * 1 / sumweight * weight);
        if (ww > 0 && hh > 0 && weight > 0) rects.add(calcMap(new MapRect(xx, yy, ww, hh, 0, r.level), ml, ns, ni, weight));
        if (r.w > r.h) {
            xx += ww;
            ww = r.w - ww;
        } else {
            yy += hh;
            hh = r.h - hh;
        }
        if (ww > 0 && hh > 0 && sumweight - weight > 0 && ni + 1 <= ne) rects.add(calcMap(new MapRect(xx, yy, ww, hh, 0, r.level), ml, ni + 1, ne, sumweight - weight));
        return rects;
    }
