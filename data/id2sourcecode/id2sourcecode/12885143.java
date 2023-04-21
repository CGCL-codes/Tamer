    private synchronized void notifySearchEngine(String sitemapUrl) {
        if (sitemapUrl == null) {
            throw new NullPointerException("illegal sitemap URL");
        }
        try {
            final URL url = new URL(sitemapUrl);
            final HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.connect();
            System.out.println("\t" + ((httpCon.getResponseCode() == 200) ? "Submitted" : "Could not submit") + " sitemap to " + sitemapUrl);
            httpCon.disconnect();
        } catch (MalformedURLException e) {
            throw new IllegalStateException("illegal URL " + sitemapUrl);
        } catch (IOException e) {
            System.out.println("\tCould not submit sitemap to " + sitemapUrl + "\n\t" + e);
        }
    }
