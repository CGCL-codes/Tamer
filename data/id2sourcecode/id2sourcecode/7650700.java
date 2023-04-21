    public void place(VobScene seafloor) {
        Placeable[] pls = (Placeable[]) placeables.toArray(new Placeable[] {});
        SortedSet impord = new TreeSet();
        SortedMap rng = new TreeMap();
        rng.put(new Integer(sea.y), new Integer(sea.y));
        rng.put(new Integer(sea.y + sea.height - 1), new Integer(sea.y + sea.height - 1));
        float sctop = 1000000000000000.0f, scbot = 0;
        for (int i = 0; i < pls.length; i++) {
            int y = (int) pls[i].pos[1];
            if (scbot < y) scbot = y;
            if (sctop > y) sctop = y;
            impord.add(pls[i]);
        }
        for (Iterator i = impord.iterator(); i.hasNext() && rng.size() != 0; ) {
            Placeable p = (Placeable) i.next();
            int yc = (int) p.pos[1];
            int h = p.buoy.getPrefHeight();
            int w = p.buoy.getPrefWidth();
            int ubound = -1, lbound = -1;
            SortedMap mp = rng.headMap(new Integer(yc));
            if (mp.size() == 0) continue;
            lbound = ((Integer) mp.get(mp.lastKey())).intValue();
            if (lbound > yc) {
                yc = lbound + h / 2;
            }
            mp = rng.tailMap(new Integer(yc));
            if (mp.size() == 0) continue;
            ubound = ((Integer) mp.firstKey()).intValue();
            if (ubound - lbound + 1 > h) {
                ubound = yc + (h - 1) / 2;
                lbound = ubound + 1 - h;
            }
            if ((ubound - lbound + 1 < h || sea.width < w) && p.buoy.constAspectScalable()) {
                int rw = sea.width, rh = ubound - lbound + 1;
                if (rw > rh * w / h) rw = rh * w / h; else rh = rw * h / w;
                w = rw;
                h = rh;
            }
            rng.put(new Integer(lbound), new Integer(ubound));
            int x = sea.x, y = lbound;
            p.buoy.put(seafloor, x, y, w, h);
        }
    }
