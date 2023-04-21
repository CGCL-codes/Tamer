    public static String get(String urlString) {
        try {
            logger.log(Level.INFO, "fetching: " + urlString);
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.addRequestProperty("Cache-Control", "no-cache,max-age=0");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "ISO-8859-1"));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(baos, "UTF-8");
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
            }
            reader.close();
            String response = baos.toString("UTF-8");
            logger.info("fetched: " + response);
            return response;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "could not fetch mensa data", e);
            return "";
        }
    }
