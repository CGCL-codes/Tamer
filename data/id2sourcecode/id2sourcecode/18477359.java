    public void draw(Graphics g) {
        Object oldColor = getAttribute("FrameColor");
        if ((mySynapse != null) && (mySynapse.isLoopBack())) setAttribute("FrameColor", Color.red); else setAttribute("FrameColor", oldColor);
        super.draw(g);
        String lbl = (String) getParam("label");
        Synapse syn = getSynapse();
        if ((lbl != null) && (syn != null)) {
            Point p1, p2;
            int i = fPoints.size();
            p1 = (Point) fPoints.elementAt(0);
            p2 = (Point) fPoints.elementAt(i - 1);
            int xMin = p1.x;
            int xMax = p2.x;
            if (xMin < p2.x) {
                xMin = p2.x;
                xMax = p1.x;
            }
            int yMin = p1.y;
            int yMax = p2.y;
            if (yMin < p2.y) {
                yMin = p2.y;
                yMax = p1.y;
            }
            Point p = new Point();
            p.x = xMin + (xMax - xMin) / 2;
            p.y = yMin + (yMax - yMin) / 2;
            if (syn.isEnabled()) g.setColor(new Color(255, 255, 255)); else g.setColor(new Color(170, 170, 170));
            g.fillRect(p.x - 6, p.y - 7, 12, 14);
            g.setColor(new Color(0, 0, 200));
            g.drawRect(p.x - 6, p.y - 7, 12, 14);
            g.drawString(lbl, p.x - 4, p.y + 5);
            g.setColor(getFrameColor());
        }
    }
