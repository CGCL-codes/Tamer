    private GeocodedAddress executeRequest(OperationContext context) throws Throwable {
        long t1 = System.currentTimeMillis();
        ReverseGeocodeParams params = context.getRequestOptions().getReverseGeocodeOptions();
        String srvCfg = context.getRequestContext().getApplicationConfiguration().getCatalogConfiguration().getParameters().getValue("openls.reverseGeocode");
        String sUrl = srvCfg + "/reverseGeocode?" + makeReverseGeocodeRequest(params.getLat(), params.getLng(), "json", 0);
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
        GeocodedAddress addr = parseReverseGeocodeResponse(sResponse);
        long t2 = System.currentTimeMillis();
        LOGGER.info("PERFORMANCE: " + (t2 - t1) + " ms spent performing service");
        return addr;
    }
