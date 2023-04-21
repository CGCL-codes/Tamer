    public boolean specialLegalMove(Message msg) {
        int i, ox, oy, nx, ny, xcheck, ycheck, xpass, ypass;
        ox = msg.oldPos.x;
        oy = msg.oldPos.y;
        nx = msg.newPos.x;
        ny = msg.newPos.y;
        xpass = nx - ox;
        ypass = ny - oy;
        xcheck = ox;
        ycheck = oy;
        if (xpass == 1 || xpass == -1) {
            xcheck = ox;
            if (ypass == 2 || ypass == -2) {
                ycheck = (oy + ny) / 2;
            } else {
                return false;
            }
        }
        if (ypass == 1 || ypass == -1) {
            ycheck = oy;
            if (xpass == 2 || xpass == -2) {
                xcheck = (ox + nx) / 2;
            } else {
                return false;
            }
        }
        if (this.cb.hasChessman(xcheck, ycheck)) {
            return false;
        }
        return true;
    }
