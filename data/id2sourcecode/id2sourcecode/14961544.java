    public static List<Address> getFromLocation(final double lat, final double lon, final int maxResults) {
        final String urlStr = "http://maps.google.com/maps/geo?q=" + lat + "," + lon + "&output=json&sensor=false";
        String response = "";
        final List<Address> results = new ArrayList<Address>();
        final HttpClient client = new DefaultHttpClient();
        Log.d("ReverseGeocode", urlStr);
        try {
            final HttpResponse hr = client.execute(new HttpGet(urlStr));
            final HttpEntity entity = hr.getEntity();
            final BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));
            String buff = null;
            while ((buff = br.readLine()) != null) {
                response += buff;
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
        JSONArray responseArray = null;
        try {
            final JSONObject jsonObject = new JSONObject(response);
            responseArray = jsonObject.getJSONArray("Placemark");
        } catch (final JSONException e) {
            return results;
        }
        Log.d("ReverseGeocode", "" + responseArray.length() + " result(s)");
        for (int i = 0; (i < responseArray.length()) && (i <= (maxResults - 1)); i++) {
            final Address addy = new Address(Locale.getDefault());
            try {
                JSONObject jsl = responseArray.getJSONObject(i);
                String addressLine = jsl.getString("address");
                if (addressLine.contains(",")) {
                    addressLine = addressLine.split(",")[0];
                }
                addy.setAddressLine(0, addressLine);
                jsl = jsl.getJSONObject("AddressDetails").getJSONObject("Country");
                try {
                    addy.setCountryName(jsl.getString("CountryName"));
                } catch (final JSONException e) {
                    e.printStackTrace();
                }
                try {
                    addy.setCountryCode(jsl.getString("CountryNameCode"));
                } catch (final JSONException e) {
                    e.printStackTrace();
                }
                try {
                    jsl = jsl.getJSONObject("AdministrativeArea");
                    addy.setAdminArea(jsl.getString("AdministrativeAreaName"));
                } catch (final JSONException e) {
                    e.printStackTrace();
                }
                try {
                    jsl = jsl.getJSONObject("SubAdministrativeArea");
                    addy.setSubAdminArea(jsl.getString("SubAdministrativeAreaName"));
                } catch (final JSONException e) {
                    e.printStackTrace();
                }
                try {
                    jsl = jsl.getJSONObject("Locality");
                    addy.setLocality(jsl.getString("LocalityName"));
                } catch (final JSONException e) {
                    e.printStackTrace();
                }
                try {
                    addy.setPostalCode(jsl.getJSONObject("PostalCode").getString("PostalCodeNumber"));
                } catch (final JSONException e) {
                    e.printStackTrace();
                }
                try {
                    addy.setThoroughfare(jsl.getJSONObject("Thoroughfare").getString("ThoroughfareName"));
                } catch (final JSONException e) {
                    e.printStackTrace();
                }
            } catch (final JSONException e) {
                e.printStackTrace();
                continue;
            }
            results.add(addy);
        }
        return results;
    }
