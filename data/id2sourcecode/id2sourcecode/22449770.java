    public static TopTracksLastFMContainer getTopTracksFromLastFM() {
        TopTracksLastFMContainer tracksContainerLastFM = null;
        try {
            URL url = new URL(URL_LAST_FM + "?method=" + METHODE_CHART_GETTOPTRACKS + "&format=" + FORMAT_JSON + "&api_key=" + API_KEY_LAST_FM);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = reader.readLine();
            Gson gson = new Gson();
            tracksContainerLastFM = gson.fromJson(line, TopTracksLastFMContainer.class);
            reader.close();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        return tracksContainerLastFM;
    }
