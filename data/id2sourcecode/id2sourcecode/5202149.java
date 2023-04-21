    private void interpret() {
        boolean dnsFound;
        List<SearchResult> results = rb.buildResults();
        logger.info("Interpreting search results...");
        for (SearchResult sr : results) {
            System.out.println("analyzing " + sr.getUrlString());
            URL url;
            try {
                dnsFound = DnsImpl.lookup(sr.getUrlString());
                if (dnsFound) {
                    url = new URL(sr.getUrlString());
                    URLConnection yc = url.openConnection();
                    yc.setConnectTimeout(5000);
                    locateAndAddPOIs(yc);
                } else System.out.println("DNS not found!");
            } catch (MalformedURLException e) {
                logger.error("Bad url exception for " + sr.getUrlString() + " " + e.getMessage());
            } catch (IOException e) {
                logger.error("I/O exception: " + e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Common exception: " + e.getMessage());
            }
        }
    }
