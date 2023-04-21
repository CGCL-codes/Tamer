    public void addMapContent(HttpServletRequest request, String mapLayerPathRoot, String idOrQuery, float minLongitude, float minLatitude, float maxLongitude, float maxLatitude) {
        ZoomLevel zoomLevel = ZoomLevel.getZoomLevel(minLongitude, minLatitude, maxLongitude, maxLatitude);
        logger.debug(zoomLevel);
        float longCentre = (maxLongitude + minLongitude) / 2;
        float latCentre = (maxLatitude + minLatitude) / 2;
        LatLongBoundingBox llbb = getBoundingBoxForZoomLevel(zoomLevel, longCentre, latCentre);
        roundLatLongValues(llbb);
        logger.debug(llbb);
        String extent = createExtent(llbb.getMinLong(), llbb.getMinLat(), llbb.getMaxLong(), llbb.getMaxLat());
        request.setAttribute(extentRequestKey, extent.toString());
        request.setAttribute(zoomRequestKey, zoomLevel.getLevel());
        request.setAttribute(minMapLongRequestKey, (int) llbb.getMinLong());
        request.setAttribute(minMapLatRequestKey, (int) llbb.getMinLat());
        request.setAttribute(maxMapLongRequestKey, (int) llbb.getMaxLong());
        request.setAttribute(maxMapLatRequestKey, (int) llbb.getMaxLat());
        addGeoServerDetails2Request(request, mapLayerPathRoot, idOrQuery, (int) llbb.getMinLong(), (int) llbb.getMinLat(), zoomLevel.getLevel());
    }
