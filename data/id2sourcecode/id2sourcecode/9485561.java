    @Override
    public GeoCoord getCoordinate(String locName) {
        GeoCoord coord = cache.get(locName);
        if (coord == null) {
            String[] values = { "404", "", "", "" };
            String urlString = googleUrlStr + locName.trim().replaceAll("\\s+", "+") + "&key=" + key;
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(urlString).openStream()));
                String line = reader.readLine();
                if (line != null) {
                    values = line.split(",");
                }
                reader.close();
            } catch (IOException exc) {
                throw new RuntimeException("Cannot parse stream from " + urlString, exc);
            }
            int status = Integer.parseInt(values[0]);
            if (status == 200) {
                double lat = Double.parseDouble(values[2]);
                double lon = Double.parseDouble(values[3]);
                coord = new GeoCoord(lat, lon);
            } else if (estimate) {
                int spcInx = locName.indexOf(" ");
                if (spcInx != -1) {
                    coord = getCoordinate(locName.substring(spcInx + 1));
                }
            }
            cache.put(locName, coord);
        }
        return coord;
    }
