    public void testAdsManagerOpts() {
        loadApi(new Runnable() {

            public void run() {
                AdsManagerOptions options = AdsManagerOptions.newInstance().setMaxAdsOnMap(2);
                assertEquals("MaxAdsOnMap 2", 2, options.getMaxAdsOnMap());
                options.setMaxAdsOnMap(10).setMinZoomLevel(7).setChannel(3.0d);
                assertEquals("MinZoomLevel 2", 7, options.getMinZoomLevel());
                assertEquals("MaxAdsOnMap 2", 10, options.getMaxAdsOnMap());
                assertEquals("Channel 2", 3.0d, options.getChannel(), 1e-8);
                options.setChannel(2.0d);
                assertEquals("Channel 3", 2.0d, options.getChannel(), 1e-8);
                assertEquals("MinZoomLevel 3", 7, options.getMinZoomLevel());
                assertEquals("MaxAdsOnMap 3", 10, options.getMaxAdsOnMap());
                options.setStyle(AdsManagerOptions.STYLE_ADUNIT);
                assertEquals("Style adunit", AdsManagerOptions.STYLE_ADUNIT, options.getStyle());
            }
        });
    }
