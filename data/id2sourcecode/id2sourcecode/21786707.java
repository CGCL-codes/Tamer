    private double makeQuery(String query) throws IOException, JSONException {
        double precision = 0;
        int relevantRecordsCount = 0;
        System.out.println("\nQuerying Yahoo for " + query);
        query = URLEncoder.encode(query, "UTF-8");
        URL url = new URL("http://boss.yahooapis.com/ysearch/web/v1/" + query + "?appid=" + yahoo_ap_id + "&count=10&format=json");
        URLConnection con = url.openConnection();
        String line;
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        String response = builder.toString();
        JSONObject json = new JSONObject(response);
        System.out.println("\nResults:");
        System.out.println("Total results = " + json.getJSONObject("ysearchresponse").getString("deephits"));
        System.out.println();
        if (Long.parseLong(json.getJSONObject("ysearchresponse").getString("deephits")) < 10) return 0;
        ja = json.getJSONObject("ysearchresponse").getJSONArray("resultset_web");
        System.out.println("\nResults:");
        for (int i = 0; i < ja.length(); i++) {
            System.out.println("Result " + (i + 1) + "\n[ ");
            JSONObject j = ja.getJSONObject(i);
            String title = Utils.filterBold(j.getString("title"));
            System.out.println("Title: " + title);
            String urlPage = Utils.filterBold(j.getString("url"));
            System.out.println("Url: " + urlPage);
            String summary = Utils.filterBold(j.getString("abstract"));
            System.out.println("Summary: " + summary);
            System.out.println("]");
            System.out.println("\nRelevant (Y/N)? ");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String userRating = null;
            HashMap<String, Double> documentVector;
            userRating = br.readLine();
            if (userRating.toLowerCase().equals("y")) {
                relevantRecordsCount++;
                documentVector = convertToTermFrequencyVector(j);
                relevant.put(i, documentVector);
            } else if (userRating.toLowerCase().equals("n")) {
                documentVector = convertToTermFrequencyVector(j);
                nonRelevant.put(i, documentVector);
            } else {
                System.out.println("Invalid Relevance Value : Enter Y/N");
            }
        }
        precision = relevantRecordsCount * 0.1;
        return precision;
    }
