    public List<AddressSPI> reverseGeoCode(String latitude, String longitude) throws LocationSPIException {
        try {
            List<AddressSPI> addresses = new ArrayList<AddressSPI>();
            String location = URLEncoder.encode(latitude + " " + longitude, "UTF-8");
            String url = "http://where.yahooapis.com/geocode?gflags=R&appid=" + appId;
            url += "&location=" + location;
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);
            String xml = null;
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                xml = EntityUtils.toString(entity);
            }
            if (xml == null || xml.trim().length() == 0) {
                return addresses;
            }
            ResultSet resultSet = this.deserializer.deserialize(xml);
            if (resultSet.isHasError()) {
                return addresses;
            }
            List<Result> results = resultSet.getResults();
            if (results == null || results.isEmpty()) {
                return addresses;
            }
            for (Result local : results) {
                AddressSPI address = new AddressSPI();
                YahooAddress resultAddress = local.getAddress();
                address.setStreet(resultAddress.getLine1());
                address.setCity(resultAddress.getCity());
                address.setState(resultAddress.getState());
                address.setCountry(resultAddress.getCountry());
                address.setZipCode(resultAddress.getZipCode());
                address.setCounty(resultAddress.getCounty());
                address.setPostal(resultAddress.getPostal());
                address.setLatitude(local.getLatitude());
                address.setLongitude(local.getLongitude());
                address.setRadius(local.getRadius());
                address.setWoeid(local.getWoeid());
                address.setWoetype(local.getWoetype());
                addresses.add(address);
            }
            return addresses;
        } catch (Throwable t) {
            log.error("", t);
            throw new LocationSPIException(t);
        }
    }
