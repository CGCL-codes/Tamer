        protected List<String> doInBackground(String... urls) {
            final List<String> result = new ArrayList<String>();
            try {
                final URL url = new URL(getResources().getString(R.string.server));
                final StringBuilder stringBuilder = new StringBuilder();
                final HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    final BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    final JSONTokener tokener = new JSONTokener(stringBuilder.toString());
                    final JSONArray arrayOfRestaurants = (JSONArray) tokener.nextValue();
                    for (int i = 0; i < arrayOfRestaurants.length(); i++) {
                        final JSONObject restaurant = arrayOfRestaurants.getJSONObject(i);
                        final String restaurantName = restaurant.getString("doingBusinessAs");
                        result.add(restaurantName);
                    }
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
