    public void setDisplayToFitMapRoutes(List<MapRoute> displayMapRouteList) {
        if (displayMapRouteList == null) {
            displayMapRouteList = mapRouteList;
        }
        if (displayMapRouteList == null || displayMapRouteList.isEmpty()) return;
        int x_min = Integer.MAX_VALUE;
        int y_min = Integer.MAX_VALUE;
        int x_max = Integer.MIN_VALUE;
        int y_max = Integer.MIN_VALUE;
        int mapZoomMax = tileController.getTileSource().getMaxZoom();
        for (MapRoute mapRoute : displayMapRouteList) {
            List<IPhoneLocation> coordinates = mapRoute.getCoordinates();
            for (IPhoneLocation coordinate : coordinates) {
                int x = OsmMercator.LonToX(coordinate.getLon(), mapZoomMax);
                int y = OsmMercator.LatToY(coordinate.getLat(), mapZoomMax);
                x_max = Math.max(x_max, x);
                y_max = Math.max(y_max, y);
                x_min = Math.min(x_min, x);
                y_min = Math.min(y_min, y);
            }
        }
        int height = Math.max(0, getHeight());
        int width = Math.max(0, getWidth());
        int newZoom = mapZoomMax;
        int x = x_max - x_min;
        int y = y_max - y_min;
        while (x > width || y > height) {
            newZoom--;
            x >>= 1;
            y >>= 1;
        }
        x = x_min + (x_max - x_min) / 2;
        y = y_min + (y_max - y_min) / 2;
        int z = 1 << (mapZoomMax - newZoom);
        x /= z;
        y /= z;
        setDisplayPosition(x, y, newZoom);
    }
