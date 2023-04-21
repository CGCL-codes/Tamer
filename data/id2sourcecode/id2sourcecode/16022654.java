    private void positionShell() {
        Shell child = mDialogShell;
        Shell parent = getParent();
        if (child != null && parent != null) {
            Rectangle parentArea = parent.getClientArea();
            Point parentLoc = parent.getLocation();
            int px = parentLoc.x;
            int py = parentLoc.y;
            int pw = parentArea.width;
            int ph = parentArea.height;
            Point childSize = sLastSize != null ? sLastSize : child.getSize();
            int cw = childSize.x;
            int ch = childSize.y;
            int x = px + (pw - cw) / 2;
            if (x < 0) x = 0;
            int y = py + (ph - ch) / 2;
            if (y < MIN_Y) y = MIN_Y;
            child.setLocation(x, y);
            child.setSize(cw, ch);
        }
    }
