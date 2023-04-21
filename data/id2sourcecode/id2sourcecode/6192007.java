    @Override
    public PlaceSPI fetchPlace(String placeReference) throws LocationSPIException {
        try {
            String url = "https://maps.googleapis.com/maps/api/place/details/xml?reference=" + placeReference + "&sensor=true&key=" + this.apiKey;
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);
            String result = null;
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity);
                PlaceSPI place = this.deserializer.deserializePlace(result);
                return place;
            }
            return null;
        } catch (Throwable t) {
            log.error(this, t);
            throw new LocationSPIException(t);
        }
    }
