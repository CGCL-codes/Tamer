    public static void makeQuery(String query) {
        System.out.println("\nQuerying for " + query);
        try {
            query = URLEncoder.encode(query, "UTF-8");
            URL url = new URL("http://ajax.googleapis.com/ajax/services/search/web?start=8&rsz=large&v=2.0&q=" + query);
            URLConnection connection = url.openConnection();
            connection.addRequestProperty("Referer", HTTP_REFERER);
            String line;
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            String response = builder.toString();
            JSONObject json = new JSONObject(response);
            System.out.println("Total results = " + json.getJSONObject("responseData").getJSONObject("cursor").getString("estimatedResultCount"));
            JSONArray ja = json.getJSONObject("responseData").getJSONArray("results");
            System.out.println("\nResults:");
            for (int i = 0; i < ja.length(); i++) {
                System.out.print((i + 1) + ". ");
                JSONObject j = ja.getJSONObject(i);
                System.out.println(j.getString("titleNoFormatting"));
                System.out.println(j.getString("url"));
                System.out.println(j.getString("content"));
            }
        } catch (Exception e) {
            System.err.println("Something went wrong...");
            e.printStackTrace();
        }
    }
