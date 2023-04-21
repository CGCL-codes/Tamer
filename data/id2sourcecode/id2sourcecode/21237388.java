    private HttpURLConnection getTrainUrl(SpantusAudioCtx ctx) {
        try {
            URL url = ctx.getTrainUrl();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
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
