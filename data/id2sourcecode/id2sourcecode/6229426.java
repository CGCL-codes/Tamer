    public synchronized void paint(Graphics g) {
        try {
            g.setClip(0, 0, getWidth(), getHeight());
            g.setColor(0x00e8e8e8);
            g.fillRect(0, 0, getWidth(), getHeight());
            Font font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL);
            g.setFont(font);
            if (autoZoom) {
                cLong = (minLongitude + maxLongitude) / 2;
                cLat = (minLatitude + maxLatitude) / 2;
            }
            cScrX = getWidth() / 2;
            cScrY = getHeight() / 2;
            if (route.length != 0) {
                for (int i = 1; i < route.length - 1; i++) drawLocation(g, route[i], 0x00606060, 0x00808080);
                Location firstCoordinates = route[0];
                Location lastCoordinates = route[route.length - 1];
                drawLocation(g, firstCoordinates, 0x000620C9, 0x00808080);
                drawLocation(g, lastCoordinates, 0x00C97D7D, 0x00808080);
                g.setColor(0x00E01010);
                g.setStrokeStyle(Graphics.DOTTED);
                g.drawLine((int) (cx), (int) (cy - 20), (int) (cx), getHeight() - font.getBaselinePosition());
                g.drawLine((int) (cx - 20), (int) (cy), getWidth() - font.getBaselinePosition(), (int) (cy));
                g.setColor(0x00010101);
                Utils.transformText(g, Utils.formatLongitude(lastCoordinates.longitude, lastCoordinates.horizontalAccuracy), Sprite.TRANS_NONE, (int) cx, getHeight(), Graphics.HCENTER | Graphics.BOTTOM);
                Utils.transformText(g, Utils.formatLatitude(lastCoordinates.latitude, lastCoordinates.horizontalAccuracy), Sprite.TRANS_ROT270, getWidth(), (int) cy, Graphics.RIGHT | Graphics.VCENTER);
                Utils.transformText(g, "Total time: " + Utils.formatTimeDiff(route[route.length - 1].timestamp - route[0].timestamp) + "  distance: " + (int) distance + "m", Sprite.TRANS_NONE, (int) (cScrX), 0, Graphics.HCENTER | Graphics.TOP);
            }
        } catch (Exception e) {
            GPStatus.log("TraceView.paint", e);
        }
    }
