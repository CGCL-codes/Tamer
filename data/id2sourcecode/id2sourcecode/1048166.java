    public void mouseDragged(MouseEvent e) {
        int nx = ox + e.getX() - W / 2;
        int ny = oy + e.getY() - H / 2;
        int dx = nx - ox;
        int dy = ny - oy;
        if (dx == 0 && dy == 0) return;
        int flop = 1 - flipflop;
        Graphics g = underneath[flop].getGraphics();
        g.drawImage(underneath[flipflop], -dx, -dy, null);
        if (dx > 0) {
            strip.x = nx + W - dx;
            strip.y = ny;
            strip.width = dx;
            strip.height = H;
            g.drawImage(robot.createScreenCapture(strip), W - dx, 0, null);
        } else if (dx < 0) {
            strip.x = nx;
            strip.y = ny;
            strip.width = -dx;
            strip.height = H;
            g.drawImage(robot.createScreenCapture(strip), 0, 0, null);
        }
        if (dy > 0) {
            strip.x = dx > 0 ? nx : nx - dx;
            strip.y = ny + H - dy;
            strip.width = W - Math.abs(dx);
            strip.height = dy;
            g.drawImage(robot.createScreenCapture(strip), dx > 0 ? 0 : -dx, H - dy, null);
        } else if (dy < 0) {
            strip.x = dx > 0 ? nx : nx - dx;
            strip.y = ny;
            strip.width = W - Math.abs(dx);
            strip.height = -dy;
            g.drawImage(robot.createScreenCapture(strip), dx > 0 ? 0 : -dx, 0, null);
        }
        flipflop = flop;
        ox = nx;
        oy = ny;
        paint(getGraphics());
    }
