    public List<AddressSPI> geoCode(String street, String city, String state, String country, String zipcode) throws LocationSPIException {
        try {
            if (street == null || street.trim().length() == 0) {
                throw new IllegalStateException("Street is required");
            }
            if (city == null || city.trim().length() == 0) {
                throw new IllegalStateException("City is required");
            }
            StringBuilder buffer = new StringBuilder();
            if (street != null && street.trim().length() > 0) {
                buffer.append(street + ",");
            }
            if (city != null && city.trim().length() > 0) {
                buffer.append(city + ",");
            }
            if (state != null && state.trim().length() > 0) {
                buffer.append(state + ",");
            }
            if (country != null && country.trim().length() > 0) {
                buffer.append(country + ",");
            }
            if (zipcode != null && zipcode.trim().length() > 0) {
                buffer.append(zipcode);
            }
            List<AddressSPI> addresses = new ArrayList<AddressSPI>();
            String location = URLEncoder.encode(buffer.toString(), "UTF-8");
            String url = "http://where.yahooapis.com/geocode?appid=" + appId;
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
                AddressSPI cour = new AddressSPI();
                YahooAddress resultAddress = local.getAddress();
                cour.setStreet(resultAddress.getLine1());
                cour.setCity(resultAddress.getCity());
                cour.setState(resultAddress.getState());
                cour.setCountry(resultAddress.getCountry());
                cour.setZipCode(resultAddress.getZipCode());
                cour.setCounty(resultAddress.getCounty());
                cour.setPostal(resultAddress.getPostal());
                cour.setLatitude(local.getLatitude());
                cour.setLongitude(local.getLongitude());
                cour.setRadius(local.getRadius());
                cour.setWoeid(local.getWoeid());
                cour.setWoetype(local.getWoetype());
                addresses.add(cour);
            }
            return addresses;
        } catch (Throwable t) {
            log.error("", t);
            throw new LocationSPIException(t);
        }
    }
