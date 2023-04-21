    private void executeRequest(OperationContext context) throws Throwable {
        long t1 = System.currentTimeMillis();
        String srvCfg = context.getRequestContext().getApplicationConfiguration().getCatalogConfiguration().getParameters().getValue("openls.determineRoute");
        String srvCfgMapService = context.getRequestContext().getApplicationConfiguration().getCatalogConfiguration().getParameters().getValue("openls.determineRoute.mapService");
        DetermineRouteParams routeParams = context.getRequestOptions().getDetermineRouteParams();
        RoutePlan rtPlan = routeParams.getRoutePlan();
        HashMap<String, Point> barriers = rtPlan.getAvoidPointList();
        GeocodedAddress[] avoidAddr = null;
        if (barriers != null) {
            avoidAddr = new GeocodedAddress[barriers.size()];
            Set<String> keys = barriers.keySet();
            Iterator<String> iter = keys.iterator();
            int cnt = 0;
            while (iter.hasNext()) {
                String key = iter.next();
                Point barrier = (Point) barriers.get(key);
                GeocodedAddress ga = new GeocodedAddress();
                ga.setX(barrier.getX());
                ga.setY(barrier.getY());
                avoidAddr[cnt] = ga;
                cnt++;
            }
        }
        HashMap<String, Point> points = rtPlan.getWayPointList();
        GeocodedAddress[] gcAddr = new GeocodedAddress[points.size()];
        Point start = points.get("start");
        GeocodedAddress gaStart = new GeocodedAddress();
        gaStart.setX(start.getX());
        gaStart.setY(start.getY());
        gcAddr[0] = gaStart;
        Point via = points.get("via");
        GeocodedAddress gaVia = new GeocodedAddress();
        gaVia.setX(via.getX());
        gaVia.setY(via.getY());
        gcAddr[1] = gaVia;
        Point end = points.get("start");
        GeocodedAddress gaEnd = new GeocodedAddress();
        gaEnd.setX(end.getX());
        gaEnd.setY(end.getY());
        gcAddr[2] = gaEnd;
        String sRouteRequest = makeRouteRequest(gcAddr, avoidAddr);
        String sUrl = srvCfg + "/solve?" + sRouteRequest;
        LOGGER.info("REQUEST=\n" + sUrl);
        URL url = new URL(sUrl);
        URLConnection conn = url.openConnection();
        String line = "";
        String sResponse = "";
        InputStream is = conn.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader rd = new BufferedReader(isr);
        while ((line = rd.readLine()) != null) {
            sResponse += line;
        }
        rd.close();
        url = null;
        DetermineRouteParams determineRouteParams = parseRouteResponse(sResponse);
        if (routeParams.getRouteInstructions().getFormat().equals("text/plain")) {
        }
        determineRouteParams.setRouteMap(routeParams.getRouteMap());
        makeExportMapRequest(determineRouteParams, srvCfgMapService);
        context.getRequestOptions().setDetermineRouteParams(determineRouteParams);
        long t2 = System.currentTimeMillis();
        LOGGER.info("PERFORMANCE: " + (t2 - t1) + " ms required to perform service");
    }
