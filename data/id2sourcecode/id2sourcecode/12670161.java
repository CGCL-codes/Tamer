        private void UpdateListItem(ListItem item) {
            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                String url = String.format("http://maps.googleapis.com/maps/api/directions/json?origin=Adliswil&destination=%f,%f&sensor=false", item.getLatidute(), item.getLongitude());
                request.setURI(new URI(url));
                HttpResponse response = client.execute(request);
                BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuffer sb = new StringBuffer("");
                String line = "";
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
                in.close();
                JSONObject jObject = new JSONObject(sb.toString());
                JSONArray jsonArray = jObject.getJSONArray("routes");
                jsonArray = jsonArray.getJSONObject(0).getJSONArray("legs");
                JSONObject leg = jsonArray.getJSONObject(0);
                JSONObject jsonObject = leg.getJSONObject("distance");
                String distance = jsonObject.getString("text");
                jsonObject = leg.getJSONObject("duration");
                String duration = jsonObject.getString("text");
                String street = leg.getString("end_address").split(",")[0];
                item.setDistance(distance);
                item.setDuration(duration);
                item.setStreet(street);
            } catch (Exception e) {
            }
        }
