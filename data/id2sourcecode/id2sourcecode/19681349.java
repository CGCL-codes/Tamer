    private Blacklist extractFromURL() {
        Blacklist oldBlacklist = getBlacklist(null);
        Blacklist newBlacklist = new Blacklist();
        try {
            URL url = new URL(blacklistURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (oldBlacklist.ifModifiedSince != null) {
                connection.setRequestProperty("If-Modified-Since", DateUtil.formatRfc822(oldBlacklist.ifModifiedSince));
            }
            if (connection.getResponseCode() == HttpURLConnection.HTTP_NOT_MODIFIED) {
                return oldBlacklist;
            }
            long lastModifiedLong = connection.getHeaderFieldDate("Last-Modified", -1);
            if (oldBlacklist.ifModifiedSince == null || oldBlacklist.ifModifiedSince.getTime() < lastModifiedLong) {
                String results = newBlacklist.readFromStream(connection.getInputStream(), true);
                newBlacklist.writeToFile(results);
                if (newBlacklist.ifModifiedSince == null && lastModifiedLong != -1) {
                    newBlacklist.ifModifiedSince = new Date(lastModifiedLong);
                }
                return newBlacklist;
            }
        } catch (MalformedURLException e) {
            mLogger.info(e);
        } catch (IOException e) {
            mLogger.info(e);
        }
        return oldBlacklist;
    }
