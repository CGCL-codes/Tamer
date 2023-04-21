        private org.json.JSONArray download_JSON(String url) {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(url);
            try {
                HttpResponse response = client.execute(request);
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String json = reader.readLine();
                JSONTokener tokener = new JSONTokener(json);
                JSONArray finalResult = new JSONArray(tokener);
                return finalResult;
            } catch (Exception ex) {
                Log.d(TAG, "Filnavne kan ikke hentes");
                return null;
            }
        }
