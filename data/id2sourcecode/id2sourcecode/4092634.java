    public static void paint(Satellite[] satellites, Graphics g, int w, int h) {
        int[] activeSatellites = new int[satellites.length];
        int activeSatelliteCount = 0;
        Satellite satellite;
        for (int i = 0; i < satellites.length; i++) {
            satellite = satellites[i];
            if (satellite != null) {
                activeSatellites[activeSatelliteCount++] = i;
                maxSignal = Math.max(maxSignal, satellite.signal);
            }
        }
        for (int i = 0; i < activeSatelliteCount; i++) {
            int satelliteNumber = activeSatellites[i];
            satellite = satellites[satelliteNumber];
            int x1 = i * w / activeSatelliteCount + 2;
            int x2 = (i + 1) * w / activeSatelliteCount - 2;
            g.setColor(0);
            g.drawRect(x1, 0, x2 - x1, h);
            int signal = 0;
            signal = satellite.signal;
            int div = (h - 1) * signal / maxSignal;
            g.setColor(192, 192, 192);
            g.fillRect(x1 + 1, 1, x2 - x1 - 1, h - div - 1);
            if (satellite.traced) g.setColor(0, 0, 255); else g.setColor(96, 96, 96);
            g.fillRect(x1 + 1, h - div, x2 - x1 - 1, div);
            g.setColor(255, 0, 0);
            g.setFont(font);
            int middle = (x1 + x2) / 2;
            g.drawString(Integer.toString(satelliteNumber + 1), middle, h - 1, Graphics.HCENTER | Graphics.BASELINE);
            g.drawString(Integer.toString(signal), middle, 1, Graphics.HCENTER | Graphics.TOP);
        }
    }
