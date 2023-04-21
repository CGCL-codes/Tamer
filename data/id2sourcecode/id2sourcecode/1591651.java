    private String open() {
        StringBuilder builder;
        try {
            URL url = new URL(adress);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", "");
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), encoding));
            String line;
            builder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
