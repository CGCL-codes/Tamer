    private HttpURLConnection getRecordUrl(RecordFormat recordFormat) {
        try {
            URL url = getRecordUrl();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", getContentType(recordFormat));
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            return conn;
        } catch (MalformedURLException e) {
            LOG.error(e);
        } catch (IOException e) {
            LOG.error(e);
        }
        return null;
    }
