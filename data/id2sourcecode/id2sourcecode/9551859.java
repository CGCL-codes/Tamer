    protected LatLng geocodeGoogle(String address, String city, String country) throws IOException {
        BufferedReader input = null;
        LatLng result = null;
        try {
            URL url = new URL(buildGooglePath(address, city, country));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            input = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = "";
            line = input.readLine();
            if (line != null && line.length() != 0) {
                String[] coords = org.apache.commons.lang.StringUtils.split(line, ",");
                if (coords[0].trim().equals("200")) {
                    result = new LatLng(Double.valueOf(coords[2]), Double.valueOf(coords[3]), Integer.valueOf(coords[0]), Integer.valueOf(coords[1]));
                } else {
                    result = new LatLng();
                    result.setStatusCode(Integer.valueOf(coords[0]));
                }
            }
        } catch (IOException ex) {
            throw new IOException("geocoding failed");
        } finally {
            if (input != null) {
                input.close();
            }
        }
        return result;
    }
