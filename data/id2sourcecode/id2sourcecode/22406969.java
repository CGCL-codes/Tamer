    private PointSet drawPath(Graphics g, int x1, int y1, int x2, int y2, double start_length, double end_length, int mode) {
        if (x1 == x2 && y1 == y2) {
            return new PointSet(x1, y1, x2, y2, -1);
        }
        int tmp_x1, tmp_y1, tmp_x2, tmp_y2;
        switch(mode) {
            case 30:
                tmp_x1 = x1;
                tmp_y1 = (y1 + y2) / 2;
                tmp_x2 = x2;
                tmp_y2 = tmp_y1;
                break;
            case 31:
                tmp_x1 = (x1 + x2) / 2;
                tmp_y1 = y1;
                tmp_x2 = tmp_x1;
                tmp_y2 = y2;
                break;
            case 39:
                tmp_x1 = x2;
                tmp_y1 = y1;
                tmp_x2 = x2;
                tmp_y2 = y2;
                x2 = x1;
                break;
            case 20:
                tmp_x1 = x1;
                tmp_y1 = y2;
                tmp_x2 = tmp_x1;
                tmp_y2 = tmp_y1;
                break;
            case 21:
            default:
                tmp_x1 = x2;
                tmp_y1 = y1;
                tmp_x2 = tmp_x1;
                tmp_y2 = tmp_y1;
                break;
        }
        PointSet ps1 = drawLine(g, x1, y1, tmp_x1, tmp_y1, start_length, 0);
        PointSet ps2 = drawLine(g, tmp_x1, tmp_y1, tmp_x2, tmp_y2, 0, 0);
        PointSet ps3 = drawLine(g, tmp_x2, tmp_y2, x2, y2, 0, end_length);
        if (tmp_x1 != tmp_x2 || tmp_y1 != tmp_y2) {
            if (ps1.x3 != tmp_x1 || ps1.y3 != tmp_y1) {
                drawPath(g, ps1.x3, ps1.y3, ps2.x2, ps2.y2, 0, 0, 20 + 1 - mode % 2);
            }
            if (ps2.x3 != tmp_x2 || ps2.y3 != tmp_y2) {
                drawPath(g, ps2.x3, ps2.y3, ps3.x2, ps3.y2, 0, 0, 20 + mode % 2);
            }
        } else {
            if (ps1.x3 != tmp_x1 || ps1.y3 != tmp_y1) {
                drawPath(g, ps1.x3, ps1.y3, ps3.x2, ps3.y2, 0, 0, 20 + 1 - mode % 2);
            }
        }
        return new PointSet(x1, y1, ps1.x2, ps1.y2, ps1.delta1, ps3.x3, ps3.y3, x2, y2, ps3.delta2);
    }
