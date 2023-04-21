    public boolean specialLegalMove(Message msg) {
        int i, ox, oy, nx, ny, xcheck, ycheck, xpass, ypass;
        ox = msg.oldPos.x;
        oy = msg.oldPos.y;
        nx = msg.newPos.x;
        ny = msg.newPos.y;
        xpass = nx - ox;
        ypass = ny - oy;
        xcheck = (nx + ox) / 2;
        ycheck = (ny + oy) / 2;
        if (ny > yLimitBig || ny < yLimitLittle) {
            return false;
        }
        if ((xpass != 2 && xpass != -2) || (ypass != 2 && ypass != -2)) {
            return false;
        }
        if (this.cb.hasChessman(xcheck, ycheck)) {
            return false;
        }
        return true;
    }
