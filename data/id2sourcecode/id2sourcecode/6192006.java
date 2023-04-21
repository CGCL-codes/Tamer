    @Override
    public List<PlaceSPI> fetchNearbyPlaces(String latitude, String longitude, List<String> types, int radius, String name) throws LocationSPIException {
        try {
            String location = latitude + "," + longitude;
            String typesParameter = null;
            if (types != null && !types.isEmpty()) {
                StringBuilder buffer = new StringBuilder();
                for (String type : types) {
                    buffer.append(type + "|");
                }
                typesParameter = buffer.toString();
            }
            String sensor = "true";
            String url = "https://maps.googleapis.com/maps/api/place/search/xml?location=" + location + "&radius=" + radius + "&sensor=" + sensor + "&key=" + apiKey;
            if (typesParameter != null) {
                typesParameter = URLEncoder.encode(typesParameter, "UTF-8");
                url += "&types=" + typesParameter;
            }
            if (name != null && name.trim().length() > 0) {
                String nameParameter = URLEncoder.encode(name, "UTF-8");
                url += "&name=" + nameParameter;
            }
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);
            String result = null;
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity);
                List<PlaceSPI> places = this.deserializer.deserializeNearByPlaces(result);
                return places;
            }
            return null;
        } catch (Throwable t) {
            log.error(this, t);
            throw new LocationSPIException(t);
        }
    }
