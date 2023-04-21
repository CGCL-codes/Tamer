    @Override
    public void paintGraphics(BasicMap map, Graphics g) {
        boolean show = this.parseBoolean("show", true);
        if (!show) return;
        boolean ids = this.parseBoolean("ids", false);
        boolean length = this.parseBoolean("length", false);
        boolean printFlow = this.parseBoolean("print flow", true);
        boolean travelTime = this.parseBoolean("show traversal time", true);
        int size, x, y, x2, y2;
        int currentTime = this.tControl.getTime();
        boolean isForward;
        for (OneWayLink oWLink : oWVN.getOneWayLinks()) {
            isForward = true;
            Link link = oWLink.getLink();
            if (link.getToNode().equals(sink)) continue;
            if (!map.isVisible(link)) continue;
            int flowValue = oWLink.getFlowAtTime(currentTime);
            if (flowValue < 0) {
                isForward = false;
                flowValue *= -1;
            }
            int cap = (int) oWLink.getMaxCapacity();
            g.setColor(getColorForFlow(flowValue, cap));
            x = map.getXonPanel(link.getFromNode());
            y = map.getYonPanel(link.getFromNode());
            x2 = map.getXonPanel(link.getToNode());
            y2 = map.getYonPanel(link.getToNode());
            g.drawLine(x, y, x2, y2);
            if (flowValue != 0) this.drawArrow(g, x, y, x2, y2, isForward);
            if (ids || length || printFlow || travelTime) {
                g.setColor(Color.black);
                int mx = (x + x2) / 2, my = (y + y2) / 2;
                mx -= 10;
                if (ids) g.drawString(link.getId().toString(), mx, my + 10);
                if (length) g.drawString("" + link.getLength(), mx, my + 5);
                if (printFlow) g.drawString(flowValue + " / " + cap, mx + 5, my);
                if (travelTime) g.drawString("tau: " + link.getFreespeed(1.), mx + 5, my + 10);
            }
        }
    }
