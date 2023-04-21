    public void xestHer1509() throws IOException {
        HashMap<String, String> testUrls = new HashMap<String, String>();
        testUrls.put("http://wayback.archive-it.org/779/20080709003013/http://www.dreamingmethods.com/uploads/lastdream/loader.swf", "project.swf");
        testUrls.put("http://wayback.archive-it.org/1094/20080923035716/http://www.dreamingmethods.com/uploads/dm_archive/mainsite/downloads/flash/Dim%20O%20Gauble/loader.swf", "map_3d.swf");
        testUrls.put("http://wayback.archive-it.org/1094/20080923040243/http://www.dreamingmethods.com/uploads/dm_archive/mainsite/downloads/flash/clearance/loader.swf", "clearance_intro.swf");
        for (String url : testUrls.keySet()) {
            HttpRecorder recorder = HttpRecorder.wrapInputStreamWithHttpRecord(getTmpDir(), this.getClass().getName(), new URL(url).openStream(), null);
            CrawlURI curi = setupCrawlURI(recorder, url);
            long startTime = System.currentTimeMillis();
            this.extractor.innerProcess(curi);
            long elapsed = System.currentTimeMillis() - startTime;
            logger.info(this.extractor.getClass().getSimpleName() + " took " + elapsed + "ms to process " + url);
            boolean foundIt = false;
            for (Link link : curi.getOutLinks()) {
                logger.info("found link: " + link);
                foundIt = foundIt || link.getDestination().toString().endsWith(testUrls.get(url));
            }
            assertTrue("failed to extract link \"" + testUrls.get(url) + "\" from " + url, foundIt);
        }
    }
