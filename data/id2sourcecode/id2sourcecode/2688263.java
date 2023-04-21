    protected void movementPhase() {
        logger.info(">>> Entering MOVEMENT phase");
        Iterator<MovableUnit> it = movableUnits.iterator();
        while (it.hasNext()) {
            MovableUnit unit = it.next();
            DMSPoint startLocation = unit.getStartMovementPoint();
            double elapsed = unit.elapsedMoveTime();
            if (elapsed <= 0.0) continue;
            int direction = unit.getBearing();
            double alpha = StrictMath.toRadians((360 - (direction - 90)) % 360);
            SceneryEditor editor = (SceneryEditor) referenceScenery.getEditor();
            SceneryPage page = editor.getMapPage();
            GraphicalViewer viewer = page.getGraphicalViewer();
            EditPart root = viewer.getContents();
            double space = 0.0;
            if (root instanceof RootMapPart) {
                RootMapPart rootPart1 = (RootMapPart) root;
                double speed = new Double(unit.getSpeed()).doubleValue();
                Point spoint = RootMapPart.dms2xy(rootPart1, new DMSPoint(DMSCoordinate.fromSeconds(new Double(speed).longValue(), DMSCoordinate.LATITUDE), DMSCoordinate.fromSeconds(new Double(speed).longValue(), DMSCoordinate.LONGITUDE)));
                space = StrictMath.abs(spoint.x * (elapsed / (60.0 * 60.0)));
            }
            double latDelta = space * StrictMath.sin(alpha);
            double lonDelta = space * StrictMath.cos(alpha);
            DMSPoint lastLocation = unit.getLocation();
            DMSPoint currentLocation = startLocation.translate(latDelta, lonDelta);
            if (root instanceof RootMapPart) {
                RootMapPart rootPart = (RootMapPart) root;
                Point prevXY = RootMapPart.dms2xy(rootPart, lastLocation);
                Point newXY = RootMapPart.dms2xy(rootPart, currentLocation);
                if (!prevXY.equals(newXY)) {
                    logger.info("Unit " + unit.toString() + " has moved to new coordinates " + newXY.toString());
                    unit.addTrace(currentLocation);
                    unit.setLatitude(currentLocation.getDMSLatitude());
                    unit.setLongitude(currentLocation.getDMSLongitude());
                }
            }
        }
    }
