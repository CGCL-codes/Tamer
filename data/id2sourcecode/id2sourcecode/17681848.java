    public void digTunnel(DisplayableObject[][] grid, int x1, int y1, int x2, int y2) {
        int xmean = (x1 + x2) / 2;
        int ymean = (y1 + y2) / 2;
        int mode = Roll.randomInt(3);
        switch(mode) {
            case 0:
                digTunnelXFirst(grid, x1, y1, x2, y2);
                break;
            case 1:
                digTunnelYFirst(grid, x1, y1, x2, y2);
                break;
            case 2:
                digTunnelXFirst(grid, x1, y1, xmean, ymean);
                digTunnelYFirst(grid, xmean, ymean, x2, y2);
                break;
            case 3:
                digTunnelYFirst(grid, x1, y1, xmean, ymean);
                digTunnelXFirst(grid, xmean, ymean, x2, y2);
                break;
        }
    }
