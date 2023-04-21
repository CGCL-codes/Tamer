    public static void setCentalizedLocationRelativeMe(Component parent, Component me) {
        if (parent == null) return;
        Point parentLoc = parent.getLocation();
        int x = 0, y = 0;
        int parentX = parentLoc.x;
        int parentY = parentLoc.y;
        int parentW = parent.getSize().width;
        int parentH = parent.getSize().height;
        int w = me.getSize().width;
        int h = me.getSize().height;
        if (w < parentW) x = parentX + (parentW - w) / 2; else x = parentX - (w - parentW) / 2;
        if (h < parentH) y = parentY + (parentH - h) / 2; else y = parentY - (h - parentH) / 2;
        me.setLocation(x, y);
    }
