    private static void makeQuery(String query, int showFromRankedesult, int limit, LinkedList<WebResult> results) {
        try {
            query = URLEncoder.encode(query, "UTF-8");
            URL url = new URL("http://ajax.googleapis.com/ajax/menzisservices/search/web?start=" + String.valueOf(showFromRankedesult) + "&rsz=large&v=1.0&q=" + query);
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
            JSONArray ja = json.getJSONObject("responseData").getJSONArray("results");
            for (int i = 0; i < ja.length() && results.size() < limit; i++) {
                JSONObject j = ja.getJSONObject(i);
                WebResult result = new WebResult();
                result.setSnippet(j.getString("content"));
                result.setTitle(j.getString("titleNoFormatting"));
                result.setUrl(j.getString("url"));
                DeliciousURLInfo del = JsonParser.parseDeliciousInfoFromURL(j.getString("url").trim());
                result.setDelicious(del);
                result.setRank(i + 1);
                result.setEngine(WebResult.GOOGLE);
                results.add(result);
            }
        } catch (Exception e) {
            System.err.println("Something went wrong...");
            e.printStackTrace();
        }
    }
