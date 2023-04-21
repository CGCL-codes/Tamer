    public ArrowLine(int begin, int end, int level, boolean direct) {
        caption = tag = "";
        this.direct = direct;
        this.begin = begin;
        this.end = end;
        this.level = level;
        this.mid = (begin + end) / 2;
        color = new Color(0, 0, 0);
        int[] xpoints = new int[7];
        int[] ypoints = new int[7];
        if (end - begin < head) end = begin + head;
        mid = (begin + end) / 2;
        ypoints[0] = level - thick;
        ypoints[1] = level - thick;
        ypoints[2] = level - thick - side;
        ypoints[3] = level;
        ypoints[4] = level + thick + side;
        ypoints[5] = level + thick;
        ypoints[6] = level + thick;
        if (direct) {
            xpoints[0] = begin;
            xpoints[1] = end - head;
            xpoints[2] = end - head;
            xpoints[3] = end;
            xpoints[4] = end - head;
            xpoints[5] = end - head;
            xpoints[6] = begin;
        } else {
            xpoints[0] = end;
            xpoints[1] = begin + head;
            xpoints[2] = begin + head;
            xpoints[3] = begin;
            xpoints[4] = begin + head;
            xpoints[5] = begin + head;
            xpoints[6] = end;
        }
        arrow = new Polygon(xpoints, ypoints, 7);
    }
