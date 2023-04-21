    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (wait) {
            if (mapImage != null) g.drawImage(mapImage, 0, 0, this);
            if (topo) g.setColor(Color.black); else g.setColor(Color.green);
            String message = "Generating Map";
            if (useUSGSMap) message = "Downloading Map";
            Font messageFont = new Font("SansSerif", Font.BOLD, 25);
            FontMetrics messageMetrics = getFontMetrics(messageFont);
            int msgHeight = messageMetrics.getHeight();
            int msgWidth = messageMetrics.stringWidth(message);
            int x = (width - msgWidth) / 2;
            int y = (height + msgHeight) / 2;
            g.setFont(messageFont);
            g.drawString(message, x, y);
            wait = false;
        } else {
            g.setColor(Color.black);
            g.fillRect(0, 0, width, height);
            Map map = null;
            if (topo) map = topoMap; else {
                if (useUSGSMap) map = usgsMap; else map = surfMap;
            }
            if (map.isImageDone()) {
                mapImage = map.getMapImage();
                g.drawImage(mapImage, 0, 0, this);
            }
            if (!topo && showDayNightShading) drawShading(g);
            if (showVehicleTrails) drawVehicleTrails(g);
            drawUnits(g);
        }
    }
