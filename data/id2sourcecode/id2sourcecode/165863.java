    public void drawSpouseLines(Graphics g) {
        Rectangle xr = bounds();
        int xw = xr.width / 2;
        int xh = xr.height;
        LinkedList sp = getSpouses();
        Person p;
        if (!hasBegun()) return;
        if (sp != null) {
            int minx = location.x;
            int maxx = location.x;
            int miny = 9999;
            int maxy = -9999;
            int count = 0;
            while (sp != null) {
                p = (Person) sp.getValue();
                if (minx > p.location.x) minx = p.location.x;
                if (maxx < p.location.x) maxx = p.location.x;
                if (miny > p.location.y) miny = p.location.y;
                if (maxy < p.location.y) maxy = p.location.y;
                count++;
                sp = (LinkedList) sp.getNext();
            }
            maxy += xh + 4;
            minx += xw;
            maxx += xw;
            sp = getSpouses();
            g.drawLine(minx, maxy, maxx, maxy);
            midx = minx + (maxx - minx) / 2;
            midy = maxy;
            while (sp != null) {
                p = (Person) sp.getValue();
                g.drawLine(p.location.x + xw, p.location.y + xh, p.location.x + xw, maxy);
                sp = (LinkedList) sp.getNext();
            }
        } else {
            midx = location.x;
            midy = location.y + xh + 4;
        }
    }
