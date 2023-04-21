    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (Wait) {
            if (mapImage != null) g.drawImage(mapImage, 0, 0, this);
            g.setColor(Color.green);
            String message = new String("Preparing Map...");
            Font alertFont = new Font("TimesRoman", Font.BOLD, 30);
            FontMetrics alertMetrics = getFontMetrics(alertFont);
            int Height = alertMetrics.getHeight();
            int Width = alertMetrics.stringWidth(message);
            int x = (300 - Width) / 2;
            int y = (300 + Height) / 2;
            g.setFont(alertFont);
            g.drawString(message, x, y);
            Wait = false;
        } else {
            g.setColor(Color.black);
            g.fillRect(0, 0, 300, 300);
            boolean image_done = false;
            SurfaceMap tempMap;
            if (topo) tempMap = topoSurface; else tempMap = marsSurface;
            if (tempMap.image_done) {
                image_done = true;
                mapImage = tempMap.getMapImage();
                g.drawImage(mapImage, 0, 0, this);
            }
            if (topo) g.setColor(Color.black); else g.setColor(Color.green);
            g.setFont(new Font("Helvetica", Font.PLAIN, 9));
            UnitInfo[] vehicleInfo = navWindow.getMovingVehicleInfo();
            int counter = 0;
            for (int x = 0; x < vehicleInfo.length; x++) {
                if (centerCoords.getAngle(vehicleInfo[x].getCoords()) < .48587D) {
                    int[] rectLocation = getUnitRectPosition(vehicleInfo[x].getCoords());
                    int[] imageLocation = getUnitDrawLocation(rectLocation, vehicleSymbol);
                    if (topo) g.drawImage(topoVehicleSymbol, imageLocation[0], imageLocation[1], this); else g.drawImage(vehicleSymbol, imageLocation[0], imageLocation[1], this);
                    if (labels) {
                        int[] labelLocation = getLabelLocation(rectLocation, vehicleSymbol);
                        g.drawString(vehicleInfo[x].getName(), labelLocation[0], labelLocation[1]);
                    }
                    counter++;
                }
            }
            g.setFont(new Font("Helvetica", Font.PLAIN, 12));
            UnitInfo[] settlementInfo = navWindow.getSettlementInfo();
            for (int x = 0; x < settlementInfo.length; x++) {
                if (centerCoords.getAngle(settlementInfo[x].getCoords()) < .48587D) {
                    int[] rectLocation = getUnitRectPosition(settlementInfo[x].getCoords());
                    int[] imageLocation = getUnitDrawLocation(rectLocation, settlementSymbol);
                    if (topo) g.drawImage(topoSettlementSymbol, imageLocation[0], imageLocation[1], this); else g.drawImage(settlementSymbol, imageLocation[0], imageLocation[1], this);
                    if (labels) {
                        int[] labelLocation = getLabelLocation(rectLocation, settlementSymbol);
                        g.drawString(settlementInfo[x].getName(), labelLocation[0], labelLocation[1]);
                    }
                }
            }
        }
    }
